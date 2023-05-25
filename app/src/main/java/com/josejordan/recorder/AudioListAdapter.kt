package com.josejordan.recorder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AudioListAdapter(private val audioFiles: Array<File>) : RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {
//class AudioListAdapter(private val data: List<String>) : RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun getItemCount() = audioFiles.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = audioFiles[position].name
        //Log.d("AudioListAdapter", "Binding view for file: ${audioFiles[position].name}")


        holder.textView.setOnClickListener { v ->
            val context = v.context
            // Implementar aquí la lógica para reproducir la grabación de audio seleccionada
        }
    }
}
