package org.hyperskill.stopwatch

import android.Manifest
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startTimer()
        } else {
            Toast.makeText(this, "Las notificaciones están desactivadas", Toast.LENGTH_SHORT).show()
        }
    }
    val channelId = "org.hyperskill"
    private var isRunning = false
    private var seconds = 0
    private var minutes = 0
    private val handler = Handler(Looper.getMainLooper())
    private var isCancelled = true
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar
    private  var inputOfUser = 0
    private lateinit var buttonSetting: Button


    private val runnable = object : Runnable {
        override fun run() {
            seconds++
            if (seconds == 60) {
                minutes++
                seconds = 0
            }

            textView.text = String.format("%02d:%02d", minutes, seconds)
            val totalSeconds = (minutes * 60) + seconds

            if (!isCancelled && inputOfUser > 0) {
                if (totalSeconds >= inputOfUser) {
                    textView.setTextColor(Color.RED)
                }
            }

            if (totalSeconds == inputOfUser){
                notification()
            }


            if (isRunning) {
                handler.postDelayed(this, 1000)
                startLoading(progressBar)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificator()


        textView = findViewById(R.id.textView)
        progressBar = findViewById(R.id.progressBar)
        buttonSetting = findViewById(R.id.settingsButton)

        progressBar.visibility = View.GONE

        findViewById<Button>(R.id.startButton).setOnClickListener {
            checkNotificationPermissionAndStart()
        }

        findViewById<Button>(R.id.resetButton).setOnClickListener {
            resetTimer()
        }

        findViewById<Button>(R.id.settingsButton).setOnClickListener{
            settingButton()
        }
    }

    private fun notification(){
       val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("StopWatch")
            .setContentText("Time exceeded")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.timer_svgrepo_com)
            .setStyle(NotificationCompat.BigTextStyle())
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()

        notificationBuilder.flags = notificationBuilder.flags or Notification.FLAG_INSISTENT

        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(393939, notificationBuilder)
    }

    private fun notificator(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "StopWatch"
            val descriptionText = "Time exceeded"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }
    private fun checkNotificationPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                startTimer()
            } else {
                requestPermissions()
            }
        } else {
            startTimer()
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun resetTimer() {
        isRunning = false

        handler.removeCallbacks(runnable)

        progressBar.visibility = View.GONE

        seconds = 0
        minutes = 0
        textView.text = "00:00"

        buttonSetting.isEnabled = true
        textView.setTextColor(Color.BLACK)
    }

    private fun startTimer() {
        if (isRunning) return
        isRunning = true

        handler.postDelayed(runnable, 1000)
        progressBar.visibility = View.VISIBLE
        buttonSetting.isEnabled = false

    }
    private fun startLoading(progressBar : ProgressBar){
        val color = getRandomColor()
        progressBar.indeterminateTintList = ColorStateList.valueOf(color)
    }

    private fun settingButton(){
        val contentView = LayoutInflater.from(this).inflate(R.layout.popup, null, false)
        var editText: EditText
        AlertDialog.Builder(this)
                .setTitle("Set upper limit in seconds")
                .setView(contentView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    editText = contentView.findViewById(R.id.upperLimitEditText)
                    val text = editText.text.toString()
                    inputOfUser = text.toIntOrNull() ?: 0
                    isCancelled = false
                }
                .setNegativeButton(android.R.string.cancel,null)
                .show()
        isCancelled = true
    }

    private fun getRandomColor(): Int {
        return Color.argb(
            255,
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }
}

