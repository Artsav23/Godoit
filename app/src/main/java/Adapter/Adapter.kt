package Adapter

import android.util.Log
import android.view.LayoutInflater
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

        fun bind(taskOption: DataTaskComponents, listener: ListenerTime) {
            this.taskOption = taskOption
            if (taskOption.useTime) {
                binding.imageView2.isVisible = true
                binding.time.isVisible = true
                binding.date.isVisible = true
                binding.time.text = SimpleDateFormat("HH:mm").format(taskOption.alarm?.timeInMillis).toString()
                binding.date.text = SimpleDateFormat("dd.MM.yyyy").format(taskOption.alarm?.timeInMillis).toString()
                listener.createAlarm(calendar = requireNotNull(taskOption.alarm), title = taskOption.title, text = taskOption.text)
            }
            binding.title.isVisible = taskOption.title.isNotEmpty()
            binding.title.text = taskOption.title
            binding.description.text = taskOption.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_notification, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position], listener)
    }

    override fun getItemCount(): Int {
       return tasks.count()
    }

    fun addTask(dataTaskComponents: DataTaskComponents) {
        tasks.add(dataTaskComponents)
        notifyDataSetChanged()
    }

    interface ListenerTime {
        fun createAlarm(title: String, text: String, calendar: Calendar)
    }
}