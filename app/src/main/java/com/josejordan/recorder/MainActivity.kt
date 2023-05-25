package com.josejordan.recorder

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var audioUri: Uri? = null

    private val audioSource = MediaRecorder.AudioSource.MIC
    private val outputFormat = MediaRecorder.OutputFormat.MPEG_4
    private val encoder = MediaRecorder.AudioEncoder.AAC

/*private fun getOutputFile(): Uri? {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val audioFileName = "myrecording_${timestamp}"

    // Creamos la subcarpeta "Grabaciones" dentro del directorio de descargas
    val storageDir = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Grabaciones")
    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }

    val audioFile = File(storageDir, audioFileName)

    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        val resolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, audioFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/Grabaciones")
        }

        resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    } else {
        Uri.fromFile(audioFile)
    }
}*/

    private fun getOutputFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val audioFileName = "myrecording_${timestamp}.mp4" // Asegúrate de agregar la extensión del archivo

        // Creamos la subcarpeta "Grabaciones" dentro del directorio de archivos privados de la aplicación
        val storageDir = File(filesDir, "Grabaciones")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File(storageDir, audioFileName)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startStopButton = findViewById<Button>(R.id.startStopButton)
        startStopButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
                startStopButton.text = "Start recording"
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_LONG).show()
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        0
                    )
                } else {
                    startRecording()
                    startStopButton.text = "Stop recording"
                    Toast.makeText(this, "Recording started", Toast.LENGTH_LONG).show()
                }
            }
            isRecording = !isRecording
        }

        val audiosButton = findViewById<Button>(R.id.audiosButton)
        audiosButton.setOnClickListener {
            val intent = Intent(this, AudioListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecording()
            findViewById<Button>(R.id.startStopButton).text = "Stop recording"
            Toast.makeText(this, "Recording started", Toast.LENGTH_LONG).show()
            isRecording = true
        } else {
            Toast.makeText(this, "Permission to record audio denied", Toast.LENGTH_LONG).show()
        }
    }

/*
    fun startRecording() {
        audioUri = getOutputFile()
        Log.d("MainActivity", "Output file: $audioUri")

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(audioSource)
            setOutputFormat(outputFormat)
            setAudioEncoder(encoder)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                setOutputFile(audioUri?.let {
                    getContentResolver().openFileDescriptor(
                        it,
                        "w"
                    )?.fileDescriptor
                })
            } else {
                setOutputFile(audioUri?.path)
            }
            try {
                prepare()
                start()
                Log.d("MainActivity", "Recording started successfully")
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to record audio", e)
                Toast.makeText(this@MainActivity, "Failed to record audio", Toast.LENGTH_LONG).show()
            }
        }
    }
*/

    fun startRecording() {
        val outputFile = getOutputFile()
        Log.d("MainActivity", "Output file: ${outputFile.absolutePath}")

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(audioSource)
            setOutputFormat(outputFormat)
            setOutputFile(outputFile.absolutePath)
            setAudioEncoder(encoder)

            try {
                prepare()
                start()
                Log.d("MainActivity", "Recording started successfully")
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to record audio", e)
                Toast.makeText(this@MainActivity, "Failed to record audio", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to stop and release recorder", e)
        } finally {
            mediaRecorder = null
            audioUri = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRecording) {
            stopRecording()
        }
    }
}
