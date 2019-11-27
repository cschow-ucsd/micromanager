package ucsd.ieeeqp.fa19.ui.new_schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.flexibleitem_event.view.*
import ucsd.ieeeqp.fa19.R

class NewPlanningEventsAdapter(
        private val events: List<FlexibleEvent>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.flexibleitem_event, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = events[position]
        with(holder.itemView) {
            textview_flexibleitem_name.text = event.name
            textview_flexibleitem_starttime.text = event.type
            textview_flexibleitem_duration.text = event.duration.toString()
            textview_flexibleitem_longitude.text = event.longitude.toString()
            textview_flexibleitem_latitude.text = event.latitude.toString()
        }
    }

}