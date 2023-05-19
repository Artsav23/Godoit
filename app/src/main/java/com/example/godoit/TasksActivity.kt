package com.example.godoit

import Adapter.Adapter
import Adapter.DataTaskComponents
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.godoit.databinding.ActivityTasksBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.Exception
import java.lang.reflect.Type
import java.util.*

class TasksActivity : AppCompatActivity(), Adapter.ListenerTime {

    private lateinit var binding: ActivityTasksBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var adapter = Adapter(this)
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycleView()
        addTask()
        registerActivityResult()
        createNotificationChanel()
        sharedPreferences  = getSharedPreferences("Tasks", MODE_PRIVATE)
        putTasks()
        adapter.changeVisibilityCheckBox(position = null, visibility = false)
    }

    private fun putTasks() {
        try {
            val mutableList = mutableListOf<DataTaskComponents>()
            val gson = Gson()
            val count = sharedPreferences.getInt("count", -1)
            Toast.makeText(this, count.toString(), Toast.LENGTH_SHORT).show()
            if (count != -1)
                for (i in 0 until count) {
                    val oneElement = gson.fromJson(sharedPreferences.getString(i.toString(), ""), DataTaskComponents::class.java)
                    mutableList.add(oneElement)
                }
            adapter.putTasks(mutableList)
        }
        catch (_: Exception) {
        }
    }

    override fun onPause() {
        val saveMutableList = adapter.getTasks()
        val gson = Gson()
        for (i in 0 until saveMutableList.size) {
            val dataTaskComponents = gson.toJson(saveMutableList[i])
            sharedPreferences.edit().putString(i.toString(), dataTaskComponents).apply()
        }
        sharedPreferences.edit().putInt("count", saveMutableList.size).apply()
        binding.toolbar.menu.clear()
        adapter.changeVisibilityCheckBox(position = null, visibility = false)
        super.onPause()
    }

    private fun registerActivityResult() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_CANCELED) {
                val dataTaskComponents =
                    it.data?.getSerializableExtra("dataTaskComponents") as DataTaskComponents

                if (it.resultCode == RESULT_OK) {
                    adapter.addTask(dataTaskComponents)
                } else if (it.resultCode == 111) {
                    val deleteAlarm = it.data?.getBooleanExtra("DeleteAlarm", false)
                    val position = it.data?.getIntExtra("position", -1)
                    adapter.changeTask(
                        dataTaskComponents = dataTaskComponents,
                        position = requireNotNull(position),
                        requireNotNull(deleteAlarm)
                    )
                }
            }
        }
    }

    private fun addTask() {
        binding.addTask.setOnClickListener {
            val intent = Intent(this, CreateTask::class.java)
            launcher.launch(intent)
        }
    }

    private fun initRecycleView() {
        binding.tasks.adapter = adapter
        binding.tasks.layoutManager = GridLayoutManager(this, 1)
    }

    override fun createAlarm(title: String, text: String, calendar: Calendar, code: Int) {
        val intent = Intent(applicationContext, Notification::class.java).apply {
            putExtra(titleExtra, title)
            putExtra(messageExtra, text)
            putExtra(notificationID, code)
        }
        val  pendingIntent = PendingIntent.getBroadcast(applicationContext, code, intent,
        FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun changeTask(taskOption: DataTaskComponents, position: Int) {
        val intent = Intent(this, CreateTask::class.java).apply {
            putExtra("dataTaskComponents", taskOption)
            putExtra("position", position)
        }
        launcher.launch(intent)
    }

    override fun changeVisibilityCheckBox(position: Int?, isVisibility: Boolean) {
        adapter.changeVisibilityCheckBox(position, isVisibility)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_tasks, menu)
        binding.toolbar.title = ""
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancel -> adapter.changeVisibilityCheckBox(position = null, visibility = false)
            R.id.clear -> {
                adapter.delete()
                adapter.changeVisibilityCheckBox(position = null, visibility = false)
            }
        }
        binding.toolbar.menu.clear()
        return true
    }

    override fun changeCheck(isChecked: Boolean, position: Int) {
        adapter.changeCheck(isChecked, position)
    }

    override fun deleteAlarmClock(code: Int) {
        val intent = Intent(this, Notification::class.java)
        val  pendingIntent = PendingIntent.getBroadcast(applicationContext, code, intent,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel() {
             val name = "Notification Channel"
             val channel = NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_DEFAULT)
             channel.description = "A description of the channel"
             val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(channel)
    }
}