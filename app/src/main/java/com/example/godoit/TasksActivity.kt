package com.example.godoit

import Adapter.Adapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.godoit.databinding.ActivityTasksBinding

class TasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycleView()
        addTask()
        registerActivityResult()
    }

    private fun registerActivityResult() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
        binding.tasks.layoutManager = GridLayoutManager(this, 2)
    }
}