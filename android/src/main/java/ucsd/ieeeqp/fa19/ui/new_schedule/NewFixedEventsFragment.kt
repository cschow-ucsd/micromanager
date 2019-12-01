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
import kotlinx.android.synthetic.main.dialog_new_fixed_event.view.*
import kotlinx.android.synthetic.main.fragment_new_fixed_events.*
import ucsd.ieeeqp.fa19.R
import ucsd.ieeeqp.fa19.viewmodel.NewScheduleViewModel

class NewFixedEventsFragment : Fragment() {
    private val newScheduleViewModel: NewScheduleViewModel by lazy {
        ViewModelProviders.of(activity!!)[NewScheduleViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_fixed_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_fixedevents_new.setOnClickListener(::showNewEventDialog)
        setupRecyclerView()
    }

    private fun showNewEventDialog(v: View) {
        val inflater = LayoutInflater.from(v.context)
        val dialogView = inflater.inflate(R.layout.dialog_new_fixed_event, view as ViewGroup, false)
        AlertDialog.Builder(context)
                .setTitle("New Fixed Event")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    val event = dialogView.createFixedEvent()
                    newScheduleViewModel.addFixedEvent(event)
                }.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .show()
    }

    private fun setupRecyclerView(): Unit = with(recyclerview_fixedevents_events) {
        layoutManager = LinearLayoutManager(context)
        val fixedAdapter = NewFixedEventsAdapter(newScheduleViewModel.fixedEventsLiveData.value!!)
        newScheduleViewModel.fixedEventsLiveData.observe(this@NewFixedEventsFragment, Observer {
            fixedAdapter.notifyDataSetChanged()
        })
        adapter = fixedAdapter
    }

    override fun onResume() {
        super.onResume()
        activity!!.setTitle(R.string.new_fixed_events_fragment)
    }
}

private fun View.createFixedEvent() = FixedEvent(
        edittext_dialogfixed_name.editText!!.text.toString(),
        edittext_dialogfixed_starttime.editText!!.text.toString().toIntOrNull() ?: 0,
        edittext_dialogfixed_endtime.editText!!.text.toString().toIntOrNull() ?: 0,
        edittext_dialogfixed_longitude.editText!!.text.toString().toDoubleOrNull() ?: 0.0,
        edittext_dialogfixed_latitude.editText!!.text.toString().toDoubleOrNull() ?: 0.0
)
