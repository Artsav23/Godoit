package com.example.godoit

import Adapter.TaskComponents
import Dialog.TimeDialogFragment
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.godoit.databinding.ActivityCreateTaskBinding
import java.util.Calendar

class CreateTask : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private var date = "23.01.2004"
    private var time = "06:30"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cancelClick()
        createDate()
        createTimer()
        listenerDialog()
        endCreate()
    }

    private fun cancelClick() {
        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun listenerDialog() {
        supportFragmentManager.setFragmentResultListener(TimeDialogFragment.TAG,this) { _, result ->
            date = requireNotNull(result.getString("calendarData"))
            time = requireNotNull(result.getString("time"))
            val timer = "$time $date"
            binding.textView2.isVisible = true
            binding.textView2.text = timer
        }
    }

    private fun createTimer() {
        binding.checkTimer.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                val dialog = TimeDialogFragment()
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, TimeDialogFragment.TAG)
            }
            else {
                binding.textView2.isVisible = false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createDate() {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val monthNumber = Calendar.getInstance().get(Calendar.MONTH)
        binding.dateTask.text = "$day ${checkMonth(monthNumber)}"

    }

    private fun checkMonth(monthNumber: Int): String {
        val mutableMonth = resources.getStringArray(R.array.month)
        return mutableMonth[monthNumber]
    }


    private fun endCreate() {
        binding.done.setOnClickListener {
            if (!binding.titleEditText.text.isNullOrEmpty() || !binding.taskEditText.text.isNullOrEmpty()) {
                val taskComponents = TaskComponents( title = binding.titleEditText.text.toString(),
                text = binding.taskEditText.text.toString(), useTime = binding.checkTimer.isChecked,
                time = time, date = date)
                val intent = Intent().apply { putExtra("taskComponents", taskComponents) }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}