package ucsd.ieeeqp.fa19.ui.new_schedule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appyvet.materialrangebar.RangeBar
import kotlinx.android.synthetic.main.fragment_new_user_prefs.*
import optaplanner.BaseUserPreferences
import ucsd.ieeeqp.fa19.R
import ucsd.ieeeqp.fa19.viewmodel.NewScheduleViewModel

class NewUserPrefsFragment : Fragment(), RangeBar.OnRangeBarChangeListener, SeekBar.OnSeekBarChangeListener {
    private val newScheduleViewModel: NewScheduleViewModel by lazy {
        ViewModelProviders.of(activity!!)[NewScheduleViewModel::class.java]
    }
    private val scheduleNameChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            newScheduleViewModel.scheduleName = s.toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_user_prefs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInputWidgets()
        setupInputListeners()
    }

    private fun setupInputWidgets() {
        rangebar_userprefs_bftime.tickStart = 0f
        rangebar_userprefs_bftime.tickEnd = BaseUserPreferences.TIME_MAX.toFloat()
        rangebar_userprefs_lunchtime.tickStart = 0f
        rangebar_userprefs_lunchtime.tickEnd = BaseUserPreferences.TIME_MAX.toFloat()
        rangebar_userprefs_dinnertime.tickStart = 0f
        rangebar_userprefs_dinnertime.tickEnd = BaseUserPreferences.TIME_MAX.toFloat()
        rangebar_userprefs_hwtime.tickStart = 0f
        rangebar_userprefs_hwtime.tickEnd = BaseUserPreferences.TIME_MAX.toFloat()
        seekbar_userprefs_soctime.max = BaseUserPreferences.TIME_MAX
        seekbar_userprefs_rectime.max = BaseUserPreferences.TIME_MAX
    }

    private fun setupInputListeners() {
        input_userprefs_schedulename.editText!!.addTextChangedListener(scheduleNameChangeListener)
        rangebar_userprefs_bftime.setOnRangeBarChangeListener(this)
        rangebar_userprefs_lunchtime.setOnRangeBarChangeListener(this)
        rangebar_userprefs_dinnertime.setOnRangeBarChangeListener(this)
        rangebar_userprefs_hwtime.setOnRangeBarChangeListener(this)
        seekbar_userprefs_soctime.setOnSeekBarChangeListener(this)
        seekbar_userprefs_rectime.setOnSeekBarChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        activity!!.setTitle(R.string.new_user_prefs_fragment)
        input_userprefs_schedulename.editText!!.apply {
            removeTextChangedListener(scheduleNameChangeListener)
            setText(newScheduleViewModel.scheduleName)
            setSelection(length())
            addTextChangedListener(scheduleNameChangeListener)
        }
    }

    override fun onTouchEnded(rangeBar: RangeBar?) {
        // Nothing
    }

    override fun onRangeChangeListener(
            rangeBar: RangeBar?,
            leftPinIndex: Int,
            rightPinIndex: Int,
            leftPinValue: String?,
            rightPinValue: String?
    ) {
        val prefs = newScheduleViewModel.userPreferences
        when (rangeBar!!.id) {
            rangebar_userprefs_bftime.id -> {
                newScheduleViewModel.userPreferences =
                        prefs.copy(_bfStartTime = leftPinIndex, _bfEndTime = rightPinIndex)
                textview_userprefs_bftime.apply {
                    setText(R.string.bf_time)
                    append(" ($leftPinIndex, $rightPinIndex)")
                }
            }
            rangebar_userprefs_lunchtime.id -> {
                newScheduleViewModel.userPreferences =
                        prefs.copy(_lunchStartTime = leftPinIndex, _lunchEndTime = rightPinIndex)
                textview_userprefs_lunchtime.apply {
                    setText(R.string.lunch_time)
                    append(" ($leftPinIndex, $rightPinIndex)")
                }
            }
            rangebar_userprefs_dinnertime.id -> {
                newScheduleViewModel.userPreferences =
                        prefs.copy(_dinnerStartTime = leftPinIndex, _dinnerEndTime = rightPinIndex)
                textview_userprefs_dinnertime.apply {
                    setText(R.string.dinner_time)
                    append(" ($leftPinIndex, $rightPinIndex)")
                }
            }
            rangebar_userprefs_hwtime.id -> {
                newScheduleViewModel.userPreferences =
                        prefs.copy(_hwStartTime = leftPinIndex, _hwEndTime = rightPinIndex)
                textview_userprefs_hwtime.apply {
                    setText(R.string.homework_time)
                    append(" ($leftPinIndex, $rightPinIndex)")
                }
            }
        }
    }

    override fun onTouchStarted(rangeBar: RangeBar?) {
        // Nothing
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val prefs = newScheduleViewModel.userPreferences
        when (seekBar!!.id) {
            seekbar_userprefs_soctime.id -> {
                newScheduleViewModel.userPreferences = prefs.copy(_socTime = progress)
                textview_userprefs_soctime.apply {
                    setText(R.string.social_time)
                    append(" ($progress)")
                }
            }
            seekbar_userprefs_rectime.id -> {
                newScheduleViewModel.userPreferences = prefs.copy(_recTime = progress)
                textview_userprefs_rectime.apply {
                    setText(R.string.recreation_time)
                    append(" ($progress)")
                }
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}