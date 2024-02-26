package com.example.unrepeatability

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AboutActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences =
            getSharedPreferences("shared_preferences", MODE_PRIVATE)


        setContentView(R.layout.activity_about)
//
        findViewById<TextView>(R.id.aboutHighscoreCountTextView).text =
            sharedPreferences.getInt("highscore_count", 0).toString()
    }
}