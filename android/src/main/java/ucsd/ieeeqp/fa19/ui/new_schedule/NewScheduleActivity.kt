package ucsd.ieeeqp.fa19.ui.new_schedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_new_schedule.*
import ucsd.ieeeqp.fa19.R
import ucsd.ieeeqp.fa19.ui.NoSwipePagerAdapter
import ucsd.ieeeqp.fa19.viewmodel.NewScheduleViewModel

class NewScheduleActivity : AppCompatActivity() {
    private val inputPagerAdapter: NoSwipePagerAdapter by lazy {
        NoSwipePagerAdapter(supportFragmentManager,
                NewPlanningEventsFragment(), NewFixedEventsFragment(), NewUserPrefsFragment())
    }
    private val newScheduleViewModel: NewScheduleViewModel by lazy {
        ViewModelProviders.of(this)[NewScheduleViewModel::class.java]
    }
    private val stageObserver = Observer<Int> { stage ->
        when (stage) {
            in 0 until inputPagerAdapter.count -> {
                viewpager_newschedule_container.setCurrentItem(stage, true)
                button_newschedule_previous.isEnabled = (stage != 0)
                button_newschedule_submit.setText(
                        if (stage == inputPagerAdapter.count - 1) R.string.schedule_new_submit
                        else R.string.schedule_new_next
                )
            }
            inputPagerAdapter.count -> submitAndFinish()
            else -> throw IllegalStateException("Invalid input stage.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)

        viewpager_newschedule_container.adapter = inputPagerAdapter
        button_newschedule_submit.setOnClickListener {
            newScheduleViewModel.incrementStage()
        }
        button_newschedule_previous.setOnClickListener {
            newScheduleViewModel.decrementStage()
        }

        newScheduleViewModel.inputStageLiveData.observe(this, stageObserver)
    }

    private fun submitAndFinish() {
        val resultIntent = Intent()
        resultIntent.putParcelableArrayListExtra(FIXED_EXTRA, newScheduleViewModel.fixedEventsLiveData.value)
        resultIntent.putParcelableArrayListExtra(FLEXIBLE_EXTRA, newScheduleViewModel.flexibleEventsLiveData.value)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val FIXED_EXTRA = "fixed events extra"
        const val FLEXIBLE_EXTRA = "flexible events extra"
    }
}