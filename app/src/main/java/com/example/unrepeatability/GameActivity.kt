package com.example.unrepeatability

import android.content.DialogInterface
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.unrepeatability.util.GameMode
import com.example.unrepeatability.util.GameStatus
import com.example.unrepeatability.util.OnGameButtonClickListener
import com.example.unrepeatability.util.SoundTheme
import com.example.unrepeatability.util.gameButtonIndexes
import com.example.unrepeatability.util.soundThemesMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity(), OnGameButtonClickListener,
    DialogInterface.OnClickListener {
    private var mode = GameMode.GENERAL
    private lateinit var mediaPlayers: List<MediaPlayer>
    private var gameButtonsFragment: GameButtonsFragment? = null
    private lateinit var gameButtons: List<Button>
    private var soundEnabled = true
    private var hardModeEnabled = false
    private var buttonSequence: ArrayList<Int> = ArrayList()
    private var sequenceIterator: Iterator<Int> = buttonSequence.iterator()
    private var gameStatus = GameStatus.APP_TURN
    private var gameCount = 0
    private var gameButtonsDelay = 500
    private var currentPressedButtonNumber = Int.MAX_VALUE
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appSettings: SharedPreferences
    private val DIALOG_RESTART = 404

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE)
        appSettings = PreferenceManager.getDefaultSharedPreferences(this)

        val soundThemeKey = appSettings.getString("sound_theme", "animal")
        val soundTheme = soundThemesMap[soundThemeKey]?: SoundTheme.ANIMALS

        mediaPlayers = soundTheme.sounds.map{ MediaPlayer.create(this, it) }

        soundEnabled = appSettings.getBoolean("sound_enabled", true)

        hardModeEnabled = appSettings.getBoolean("hard", false)

        setContentView(R.layout.activity_game)

        mode = @Suppress("DEPRECATION") intent.extras
            ?.getSerializable("mode") as GameMode

        val gameStatusTextView = findViewById<TextView>(R.id.gameStatusTextView)
        findViewById<TextView>(R.id.gameHighscoreTextView).text =
            sharedPreferences.getInt("highscore_count", 0).toString()

        if (mode == GameMode.FREE) {
            gameStatusTextView.text = getString(R.string.game_status_free)
            findViewById<LinearLayout>(R.id.countLayout).visibility = INVISIBLE
        } else {
            gameStatusTextView.text = getString(R.string.game_status_general)
            findViewById<LinearLayout>(R.id.countLayout).visibility = VISIBLE
            gameButtonsDelay = appSettings.getInt("sound_delay", 500)
        }
    }

    override fun onStart() {
        super.onStart()
        findViewById<TextView>(R.id.gameCurrentCountTextView).text = gameCount.toString()
        if (mode == GameMode.GENERAL) {
            gameButtonsFragment = supportFragmentManager
                .findFragmentById(R.id.gameButtonsFragmentContainerView) as GameButtonsFragment?
            gameButtons = gameButtonsFragment?.buttons!!
        }
    }

    override fun onResume() {
        super.onResume()
        if (mode == GameMode.GENERAL && gameStatus == GameStatus.APP_TURN) {
            lifecycleScope.launch {
//                gameplayMain()
                gameButtons.forEach { it.isEnabled = false }
                delay(1000)
                findViewById<TextView>(R.id.gameStatusTextView).text =
                    getString(R.string.game_status_attention)
                delay(1500)
                buttonSequence.add(gameButtonIndexes.random())
                sequenceIterator = buttonSequence.iterator()
                while (sequenceIterator.hasNext()) {
                    val buttonNumber = sequenceIterator.next()
                    if (!hardModeEnabled) gameButtons[buttonNumber].isPressed = true
                    if (soundEnabled) mediaPlayers[buttonNumber].start()
                    delay(1000)
                    gameButtons[buttonNumber].isPressed = false
                    delay(gameButtonsDelay.toLong())
                }
                gameButtons.forEach { it.isEnabled = true }
                sequenceIterator = buttonSequence.iterator()
                gameStatus = GameStatus.USER_TURN
                findViewById<TextView>(R.id.gameStatusTextView).text =
                    getString(R.string.game_status_user_turn)
            }
        }

        if (mode == GameMode.GENERAL && gameStatus == GameStatus.END_TURN) {
            AlertDialog.Builder(this@GameActivity).setTitle("Warning!")
                .setMessage("Sorry, you lose! Your score: $gameCount")
                .setPositiveButton("Restart", this@GameActivity)
                .create()
                .show()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayers.forEach { if (it.isPlaying) it.stop() }
    }

    override fun onGameButtonClick(buttonNumber: Int) {
            currentPressedButtonNumber = buttonNumber
            if (soundEnabled) mediaPlayers[buttonNumber].start()

        if (gameStatus == GameStatus.USER_TURN && sequenceIterator.hasNext()) {
            if (currentPressedButtonNumber != sequenceIterator.next()) {

                gameStatus = GameStatus.END_TURN
            } else if (!sequenceIterator.hasNext()) {
                gameCount++
                findViewById<TextView>(R.id.gameCurrentCountTextView).text = gameCount.toString()
                gameStatus = GameStatus.APP_TURN
                val highsscore = sharedPreferences.getInt("highscore_count", 0)
                if (gameCount > highsscore) {
                    with(sharedPreferences.edit()) {
                        putInt("highscore_count", gameCount)
                        apply()
                    }
                    findViewById<TextView>(R.id.gameHighscoreTextView).text = gameCount.toString()
                }
            }
            onResume()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            gameCount = 0
            findViewById<TextView>(R.id.gameCurrentCountTextView).text = gameCount.toString()
            buttonSequence = ArrayList()
            gameStatus = GameStatus.APP_TURN
            onResume()
        }
    }


}