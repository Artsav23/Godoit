package Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.godoit.R
import com.example.godoit.databinding.ActivityItemNotificationBinding

class Adapter: RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var tasks = mutableListOf<TaskComponents>()

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ActivityItemNotificationBinding.bind(item)

        fun bind(taskOption: TaskComponents) {
            if (taskOption.useTime) {
                binding.imageView2.isVisible = true
                binding.time.text = taskOption.time
            }
            binding.title.text = taskOption.title
            binding.description.text = taskOption.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_notification, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
       return tasks.count()
    }
}