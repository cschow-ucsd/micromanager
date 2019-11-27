package ucsd.ieeeqp.fa19.ui.new_schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fixeditem_event.view.*
import ucsd.ieeeqp.fa19.R

class NewFixedEventsAdapter(
        private val events: List<FixedEvent>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.fixeditem_event, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = events[position]
        with(holder.itemView) {
            textview_fixeditem_name.text = event.name
            textview_fixeditem_starttime.text = event.startTime.toString()
            textview_fixeditem_endtime.text = event.endTime.toString()
            textview_fixeditem_longitude.text = event.longitude.toString()
            textview_fixeditem_latitude.text = event.latitude.toString()
        }
    }
}