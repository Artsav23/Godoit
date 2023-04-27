package com.example.godoit

import Adapter.DataTaskComponents
import Dialog.TimeDialogFragment
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.godoit.databinding.ActivityCreateTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class CreateTask : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private var alarmData: Calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        set(Calendar.MONTH, Calendar.MONTH)
        set(Calendar.DATE, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

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
            alarmData.set(Calendar.YEAR, requireNotNull(result.getInt("year")))
            alarmData.set(Calendar.MONTH, requireNotNull(result.getInt("month")))
            alarmData.set(Calendar.DAY_OF_MONTH, requireNotNull(result.getInt("day")))
            alarmData.set(Calendar.HOUR_OF_DAY, requireNotNull(result.getInt("hour")))
            alarmData.set(Calendar.MINUTE, requireNotNull(result.getInt("minute")))



            val timer = "${SimpleDateFormat("HH:mm dd.MM.yyyy").format(alarmData.timeInMillis)}"
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

                val dataTaskComponents = DataTaskComponents( title = binding.titleEditText.text.toString(),
                text = binding.taskEditText.text.toString(), useTime = binding.checkTimer.isChecked, alarm = alarmData)

                val intent = Intent().apply { putExtra("dataTaskComponents", dataTaskComponents) }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}