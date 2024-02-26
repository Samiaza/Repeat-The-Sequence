package com.example.unrepeatability

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.Timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    private lateinit var startJingle: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE)
        if (!sharedPreferences.contains("highscore_count")) {
            with(sharedPreferences.edit()) {
                putInt("highscore_count", 0)
                apply()
            }
        }

        super.onCreate(savedInstanceState)
        startJingle = MediaPlayer.create(this, R.raw.game_start)
        setContentView(R.layout.activity_main)

        startJingle.start()
        Timer().schedule(timerTask {
            startActivity(Intent(this@MainActivity, MenuActivity::class.java))
            startJingle.release()
            finish()
        }, 2500)
    }
}