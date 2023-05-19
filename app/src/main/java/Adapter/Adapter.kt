package Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.godoit.R
import com.example.godoit.databinding.ActivityItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.*
class Adapter(private val listener: ListenerTime): RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var tasks = mutableListOf<DataTaskComponents>()

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ActivityItemNotificationBinding.bind(item)
        private lateinit var taskOption: DataTaskComponents

        fun bind(taskOption: DataTaskComponents, listener: ListenerTime, position: Int) {
            this.taskOption = taskOption
            createTask()
            alarmClock(listener)
            itemView.setOnLongClickListener {

                listener.changeVisibilityCheckBox(position, isVisibility = true)
                false
            }
            viewTouchListener(listener, position)
            binding.checkBox.isVisible = taskOption.checkVisibility
            binding.checkBox.isChecked = taskOption.check.also {
                binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isClickable)
                        listener.changeCheck(isChecked, position)
                }
            }


        }

        @SuppressLint("ClickableViewAccessibility")
        private fun viewTouchListener(listener: ListenerTime, position: Int) {
            itemView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP && (event.eventTime - event.downTime) < 300) {
                    listener.changeTask(taskOption, position)
                }
                false
            }
        }

        private fun alarmClock(listener: ListenerTime) {
            if (taskOption.useTime)
                 addAlarmClock(listener)
            else {
                binding.imageView2.isVisible = false
                binding.time.isVisible = false
                binding.date.isVisible = false
            }
        }

        private fun checkTime(alarmData: Calendar): Boolean {
            val minute = alarmData.get(Calendar.MINUTE)
            val hour = alarmData.get(Calendar.HOUR_OF_DAY)
            val day = alarmData.get(Calendar.DAY_OF_MONTH)
            val month = alarmData.get(Calendar.MONTH)
            val  year = alarmData.get(Calendar.YEAR)
            return (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && month == Calendar.getInstance().get(Calendar.MONTH) &&
                    Calendar.getInstance().get(Calendar.YEAR) == year) &&
                    ((hour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY) &&
                    minute > Calendar.getInstance().get(Calendar.MINUTE)) || (hour > Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))
        }

        private fun checkDate(alarmData: Calendar): Boolean {
            val day = alarmData.get(Calendar.DAY_OF_MONTH)
            val month = alarmData.get(Calendar.MONTH)
            val  year = alarmData.get(Calendar.YEAR)
            return (Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance().get(Calendar.MONTH) < month) ||
                    (Calendar.getInstance().get(Calendar.YEAR) < year) ||
                    (Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance().get(Calendar.MONTH) == month &&
                            day > Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        }

        private fun createTask() {
            binding.title.isVisible = taskOption.title.isNotEmpty()
            binding.title.text = taskOption.title
            binding.description.text = taskOption.text
        }

        @SuppressLint("SimpleDateFormat")
        private fun addAlarmClock(listener: ListenerTime) {
            binding.imageView2.isVisible = true
            binding.time.isVisible = true
            binding.date.isVisible = true
            binding.time.text = SimpleDateFormat("HH:mm").format(taskOption.alarm?.timeInMillis).toString()
            binding.date.text = SimpleDateFormat("dd.MM.yyyy").format(taskOption.alarm?.timeInMillis).toString()
            if (checkTime(requireNotNull(taskOption.alarm)) || checkDate(requireNotNull(taskOption.alarm)))
                listener.createAlarm(calendar = requireNotNull(taskOption.alarm), title = taskOption.title, text = taskOption.text,
                code = requireNotNull(taskOption.codeNotification))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_notification, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position], listener, position)
    }

    override fun getItemCount(): Int {
       return tasks.count()
    }

    fun addTask(dataTaskComponents: DataTaskComponents) {
        tasks.add(dataTaskComponents)
        notifyDataSetChanged()
    }

    fun changeTask(dataTaskComponents: DataTaskComponents, position: Int, deleteAlarmClock: Boolean) {
        tasks[position] = dataTaskComponents
        if (deleteAlarmClock) {
            listener.deleteAlarmClock(requireNotNull(tasks[position].codeNotification))
        }
        notifyDataSetChanged()
    }

    fun changeVisibilityCheckBox(position: Int?, visibility: Boolean) {
        tasks.forEach {
            it.checkVisibility = visibility
            it.check = false
        }
        if (position != null) tasks[position].check = true
        notifyDataSetChanged()
    }

    fun changeCheck(isChecked: Boolean, position: Int) {
        tasks[position].check = isChecked
    }

    fun delete() {
        for (i in 0 until tasks.size) {
            if (tasks[i].useTime) listener.deleteAlarmClock(requireNotNull(tasks[i].codeNotification))
        }
        tasks.removeAll { it.check }
        notifyDataSetChanged()
    }

    fun getTasks(): MutableList<DataTaskComponents> {
        return tasks
    }

    fun putTasks(mutableList: MutableList<DataTaskComponents>) {
        tasks = mutableList
        notifyDataSetChanged()
    }

    interface ListenerTime {
        fun createAlarm(title: String, text: String, calendar: Calendar, code: Int)
        fun changeTask (taskOption: DataTaskComponents, position: Int)
        fun changeVisibilityCheckBox (position: Int?, isVisibility: Boolean)
        fun changeCheck(isChecked: Boolean, position: Int)
        fun deleteAlarmClock(code: Int)
    }
}

