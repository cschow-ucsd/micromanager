package ucsd.ieeeqp.fa19.ui.display_schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import call.MmSolutionResponse
import kotlinx.android.synthetic.main.activity_display_schedule.*
import optaplanner.OpPID
import ucsd.ieeeqp.fa19.R
import ucsd.ieeeqp.fa19.ui.new_schedule.FixedEvent
import ucsd.ieeeqp.fa19.ui.new_schedule.NewFixedEventsAdapter
import ucsd.ieeeqp.fa19.viewmodel.MmServiceViewModel

class DisplayScheduleActivity : AppCompatActivity() {
    companion object {
        const val SOLVE_RESULT_OP_PID = "solve result op pid"
    }

    private val mmServiceViewModel: MmServiceViewModel by lazy {
        ViewModelProviders.of(this)[MmServiceViewModel::class.java]
    }
    private val displayEvents = mutableListOf<FixedEvent>()
    private lateinit var adapter: NewFixedEventsAdapter
    private val observer = Observer<MmSolutionResponse> { response ->
        val allEvents = response.fixedEvents + response.plannedEvents
        val sortedEvents = allEvents.map {
            FixedEvent(it.name, it.startTime, it.endTime, it.longitude, it.latitude)
        }.sortedBy { it.startTime }
        displayEvents.clear()
        displayEvents.addAll(sortedEvents)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_schedule)

        mmServiceViewModel.apply {
            initService(null)
            getMmSolvedScheduleLiveData().observe(this@DisplayScheduleActivity, observer)
            getSolveResult(OpPID(intent.getStringExtra(SOLVE_RESULT_OP_PID)!!))
        }
        recyclerview_displayschedule_events.layoutManager = LinearLayoutManager(this)
        adapter = NewFixedEventsAdapter(displayEvents)
        recyclerview_displayschedule_events.adapter = adapter
    }
}
