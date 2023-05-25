package com.josejordan.recorder

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AudioListAdapter(private var audioFiles: Array<File>) :
    RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {


    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        textView.setBackgroundResource(R.drawable.list_item_background) // Añade esta línea
        return ViewHolder(textView)
    }


    override fun getItemCount() = audioFiles.size


    /*
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = audioFiles[position].name
            holder.textView.setOnClickListener {
                // Obtiene la URI del archivo de audio y la reproduce
                val audioFile = audioFiles[position]
                val mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioFile.absolutePath)
                    prepare()
                    start()
                }
                // Detiene y libera el reproductor después de la reproducción
                mediaPlayer.setOnCompletionListener { mp ->
                    mp.stop()
                    mp.release()
                }
            }




        }
    */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audioFile = audioFiles[position]
        holder.textView.text = audioFile.name
        holder.textView.isSelected = false // inicialmente no seleccionado

        holder.textView.setOnClickListener {
            // Marcar como seleccionado al inicio de la reproducción
            holder.textView.isSelected = true

            val mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFile.absolutePath)
                prepare()
                start()
            }

            mediaPlayer.setOnCompletionListener { mp ->
                mp.stop()
                mp.release()

                // Desmarcar como seleccionado cuando la reproducción finaliza
                holder.textView.isSelected = false
            }
        }
        holder.textView.setOnLongClickListener { v ->
            // Marcar como seleccionado al inicio de la acción de pulsación larga
            holder.textView.isSelected = true

            // Muestra un cuadro de diálogo de confirmación cuando se mantiene presionado el elemento
            AlertDialog.Builder(v.context).apply {
                setTitle("Delete recording")
                setMessage("Are you sure you want to delete this recording?")
                setPositiveButton("Yes") { _, _ ->
                    // Elimina el archivo si el usuario confirma
                    if (audioFile.delete()) {
                        // Actualiza la lista de archivos y notifica al adaptador
                        audioFiles = audioFiles.filterIndexed { index, _ -> index != position }
                            .toTypedArray()
                        notifyDataSetChanged()
                        Toast.makeText(v.context, "Recording deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(v.context, "Failed to delete recording", Toast.LENGTH_SHORT)
                            .show()
                    }

                    // Desmarcar como seleccionado cuando la acción se ha completado
                    holder.textView.isSelected = false
                }
                setNegativeButton("No") { _, _ ->
                    // Desmarcar como seleccionado si el usuario cancela la acción
                    holder.textView.isSelected = false
                }
                show()
            }
            true
        }
        holder.textView.setOnLongClickListener { v ->
            // Marcar como seleccionado al inicio de la acción de pulsación larga
            holder.textView.isSelected = true

            // Muestra un cuadro de diálogo de confirmación cuando se mantiene presionado el elemento
            AlertDialog.Builder(v.context).apply {
                setTitle("Delete recording")
                setMessage("Are you sure you want to delete this recording?")
                setPositiveButton("Yes") { _, _ ->
                    // Elimina el archivo si el usuario confirma
                    if (audioFile.delete()) {
                        // Actualiza la lista de archivos y notifica al adaptador
                        audioFiles = audioFiles.filterIndexed { index, _ -> index != position }
                            .toTypedArray()
                        notifyDataSetChanged()
                        Toast.makeText(v.context, "Recording deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(v.context, "Failed to delete recording", Toast.LENGTH_SHORT)
                            .show()
                    }

                    // Desmarcar como seleccionado cuando la acción se ha completado
                    holder.textView.isSelected = false
                }
                setNegativeButton("No") { _, _ ->
                    // Desmarcar como seleccionado si el usuario cancela la acción
                    holder.textView.isSelected = false
                }
                show()
            }
            true
        }

    }


}
