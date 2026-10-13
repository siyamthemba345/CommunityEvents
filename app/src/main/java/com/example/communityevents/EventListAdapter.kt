package com.example.communityevents

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Blob

class EventListAdapter(private val eventsList: List<Events_Data>) :
    RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEvent: ImageView = itemView.findViewById(R.id.img_event)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_event_title)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_event_location)
        val tvTime: TextView = itemView.findViewById(R.id.tv_event_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventsList[position]
        holder.tvTitle.text = event.Event
        holder.tvLocation.text = event.Event_location
        holder.tvTime.text = "Time: ${event.Event_Time}:00"

        val blob: Blob = event.Image
        try {
            val bytes = blob.getBytes(1, blob.length().toInt())
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            holder.imgEvent.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = eventsList.size
}

typealias eventListAdapter = EventListAdapter