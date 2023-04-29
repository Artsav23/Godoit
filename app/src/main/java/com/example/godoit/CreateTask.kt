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
    private var position: Int = -1
    private lateinit var dataTaskComponents: DataTaskComponents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkChange()
        cancelClick()
        createDate()
        createTimer()
        listenerDialog()
        endCreateOrChange()
    }

    private fun checkChange() {
        if (intent.getIntExtra("position", -1) != -1) {
            position = intent.getIntExtra("position", -1)
            dataTaskComponents = intent.getSerializableExtra("dataTaskComponents") as DataTaskComponents
            alarmData = dataTaskComponents.alarm as Calendar

            binding.titleEditText.setText(dataTaskComponents.title)
            binding.taskEditText.setText(dataTaskComponents.text)
            binding.checkTimer.isChecked = dataTaskComponents.useTime
            if (dataTaskComponents.useTime) {
                val timer = "${SimpleDateFormat("HH:mm dd.MM.yyyy").format(alarmData.timeInMillis)}"
                binding.textView2.isVisible = true
                binding.textView2.text = timer
            }
        }
    }

    private fun cancelClick() {
        binding.cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
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


    private fun endCreateOrChange() {
        binding.done.setOnClickListener {
            if (!binding.titleEditText.text.isNullOrEmpty() || !binding.taskEditText.text.isNullOrEmpty()) {
                val intent = Intent()
                var code: Int? = null
                val resultCode: Int
                if (position != -1) {
                    code = dataTaskComponents.codeNotification
                    intent.putExtra("position", position)
                    resultCode = 111
                }
                else {
                    if (binding.checkTimer.isChecked) code = createCode()
                    resultCode = RESULT_OK
                }
                dataTaskComponents = DataTaskComponents(title = binding.titleEditText.text.toString(),
                    text = binding.taskEditText.text.toString(),
                    useTime = binding.checkTimer.isChecked, alarm = alarmData, codeNotification = code)
                intent.putExtra("dataTaskComponents", dataTaskComponents)
                setResult(resultCode, intent)
                finish()
            }
        }
    }

    private fun createCode(): Int {
        var code = 0
        val symbols = (1..9999999)
        for (i in 1..(10..200).random()) {
            code += symbols.random()
        }
        return code
    }
}