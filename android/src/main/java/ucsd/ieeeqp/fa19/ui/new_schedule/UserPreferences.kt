package ucsd.ieeeqp.fa19.ui.new_schedule

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import optaplanner.BaseUserPreferences

@Parcelize
data class UserPreferences(
        val _bfStartTime: Int = 0,
        val _bfEndTime: Int = 0,
        val _lunchStartTime: Int = 0,
        val _lunchEndTime: Int = 0,
        val _dinnerStartTime: Int = 0,
        val _dinnerEndTime: Int = 0,
        val _socTime: Int = 0,
        val _recTime: Int = 0
) : BaseUserPreferences(
        _bfStartTime,
        _bfEndTime,
        _lunchStartTime,
        _lunchEndTime,
        _dinnerStartTime,
        _dinnerEndTime,
        _socTime,
        _recTime
), Parcelable