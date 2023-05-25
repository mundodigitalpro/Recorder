package com.josejordan.recorder

import android.os.Environment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AudioListActivity : AppCompatActivity() {

    private lateinit var audioFiles: Array<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        populateAudioList()
    }

/*    private fun populateAudioList() {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val recordingDir = File(storageDir, "Grabaciones")
        audioFiles = recordingDir.listFiles() ?: arrayOf()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = AudioListAdapter(audioFiles)
    }*/

    private fun populateAudioList() {
        val recordingDir = File(filesDir, "Grabaciones")
        audioFiles = recordingDir.listFiles() ?: arrayOf()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = AudioListAdapter(audioFiles)
    }

}


