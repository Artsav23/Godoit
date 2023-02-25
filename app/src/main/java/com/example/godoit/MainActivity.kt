package com.example.godoit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var txt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txt = findViewById(R.id.textView)
        txt.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_main_activity))
        clickStart()
    }

    private fun clickStart() {
        val img = findViewById<ImageView>(R.id.imageView)
        img.setOnClickListener {
            startActivity(Intent(this, TasksActivity::class.java))
            finish()
        }
    }
}