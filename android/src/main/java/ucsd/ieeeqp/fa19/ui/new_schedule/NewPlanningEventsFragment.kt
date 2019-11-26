package ucsd.ieeeqp.fa19.ui.new_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ucsd.ieeeqp.fa19.R
import ucsd.ieeeqp.fa19.viewmodel.NewScheduleViewModel

class NewPlanningEventsFragment: Fragment() {
    val newScheduleViewModel: NewScheduleViewModel by lazy {
        ViewModelProviders.of(activity!!)[NewScheduleViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_planning_events, container, false)
    }
}