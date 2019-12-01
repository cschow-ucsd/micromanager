package ucsd.ieeeqp.fa19.ui.new_schedule

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_new_flexible_event.view.*
import kotlinx.android.synthetic.main.fragment_new_planning_events.*
import ucsd.ieeeqp.fa19.R
import ucsd.ieeeqp.fa19.viewmodel.NewScheduleViewModel

class NewPlanningEventsFragment : Fragment() {
    private val newScheduleViewModel: NewScheduleViewModel by lazy {
        ViewModelProviders.of(activity!!)[NewScheduleViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_planning_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_planningevents_new.setOnClickListener(::showNewEventDialog)
        setupRecyclerView()
    }

    private fun showNewEventDialog(v: View) {
        val inflater = LayoutInflater.from(v.context)
        val dialogView = inflater.inflate(R.layout.dialog_new_flexible_event, view as ViewGroup, false)
        AlertDialog.Builder(context)
                .setTitle("New Flexible Event")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    val event = dialogView.createFlexibleEvent()
                    newScheduleViewModel.addFlexibleEvent(event)
                }.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .show()
    }

    private fun setupRecyclerView(): Unit = with(recyclerview_planningevents_events) {
        layoutManager = LinearLayoutManager(context)
        val flexibleAdapter = NewPlanningEventsAdapter(newScheduleViewModel.flexibleEventsLiveData.value!!)
        newScheduleViewModel.flexibleEventsLiveData.observe(this@NewPlanningEventsFragment, Observer {
            flexibleAdapter.notifyDataSetChanged()
        })
        adapter = flexibleAdapter
    }

    override fun onResume() {
        super.onResume()
        activity!!.setTitle(R.string.new_planning_events_fragment)
    }
}

private fun View.createFlexibleEvent() = FlexibleEvent(
        edittext_dialogflexible_name.editText!!.text.toString(),
        edittext_dialogflexible_type.editText!!.text.toString(),
        edittext_dialogflexible_duration.editText!!.text.toString().toIntOrNull() ?: 0,
        edittext_dialogflexible_longitude.editText!!.text.toString().toDoubleOrNull() ?: 0.0,
        edittext_dialogflexible_latitude.editText!!.text.toString().toDoubleOrNull() ?: 0.0
)