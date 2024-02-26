package com.example.unrepeatability

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.unrepeatability.util.GameMode

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val clickListener = View.OnClickListener {
            when (it.id) {
                R.id.newGameButton -> startActivity(Intent(this, GameActivity::class.java)
                    .apply { putExtra("mode", GameMode.GENERAL) })
                R.id.freeGameButton -> startActivity(Intent(this, GameActivity::class.java)
                    .apply { putExtra("mode", GameMode.FREE) })
                R.id.settingsButton -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.aboutButton -> startActivity(Intent(this, AboutActivity::class.java))
            }
        }

        findViewById<Button>(R.id.newGameButton).apply { setOnClickListener(clickListener) }
        findViewById<Button>(R.id.freeGameButton).apply { setOnClickListener(clickListener) }
        findViewById<Button>(R.id.settingsButton).apply { setOnClickListener(clickListener) }
        findViewById<Button>(R.id.aboutButton).apply { setOnClickListener(clickListener) }
    }
}