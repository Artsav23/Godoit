package com.example.godoit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.godoit.databinding.ActivityCreateTaskBinding
import java.util.Calendar

class CreateTask : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createDate()
    }

    private fun createDate() {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val monthNumber = Calendar.getInstance().get(Calendar.MONTH)
        binding.dateTask.text = "$day ${checkmonth(monthNumber)}"

    }

    private fun checkmonth(monthNumber: Int): String {
        val mutableMonth = arrayOf("Января","Февраля","Марта","Апреля","Мая","Июля","Июня",
            "Августа","Сентября","Октября","Ноября","Декабря")
        return mutableMonth[monthNumber]
    }
}