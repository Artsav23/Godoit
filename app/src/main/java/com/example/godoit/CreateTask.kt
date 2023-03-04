package com.example.godoit

import Dialog.TimeDialogFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentTransaction
import com.example.godoit.databinding.ActivityCreateTaskBinding
import java.util.Calendar

class CreateTask : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private var title = ""
    private var text = "Нет текста"
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createDate()
        createTimer()
        listenerDialog()
    }

    private fun listenerDialog() {
        supportFragmentManager.setFragmentResultListener(TimeDialogFragment.TAG,this) { _, result ->
            Toast.makeText(this, result.getString("time"), Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTimer() {
        binding.checkTimer.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                val dialog = TimeDialogFragment()
                dialog.isCancelable = false
                dialog.setTargetFragment(TimeDialogFragment(),TimeDialogFragment.TAG.toInt())
                dialog.show(supportFragmentManager, TimeDialogFragment.TAG)
            }
        }
    }

    private fun createDate() {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val monthNumber = Calendar.getInstance().get(Calendar.MONTH)
        binding.dateTask.text = "$day ${checkMonth(monthNumber)}"

    }

    private fun checkMonth(monthNumber: Int): String {
        val mutableMonth = arrayOf("Января","Февраля","Марта","Апреля","Мая","Июля","Июня",
            "Августа","Сентября","Октября","Ноября","Декабря")
        return mutableMonth[monthNumber]
    }
}