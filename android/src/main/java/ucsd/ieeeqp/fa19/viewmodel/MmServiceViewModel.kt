package ucsd.ieeeqp.fa19.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import call.MmProblemRequest
import call.MmSolveStatus
import kotlinx.coroutines.launch
import ucsd.ieeeqp.fa19.service.MmService
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

class MmServiceViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG: String = "MmServiceViewModel"
    private var mmService: MmService? = null
    private val mmLoginStateLiveData = MutableLiveData(NOT_LOGGED_IN)
    private val mmSolveStatusLiveData = MutableLiveData<ArrayList<MmSolveStatus>>(ArrayList())
    private val queryTimer: Timer = Timer()
    private var queryStatusTask: TimerTask? = null

    fun initService(serverAuthCode: String?) {
        invalidateService()
        mmService = MmService(getApplication(), serverAuthCode)
    }

    private fun invalidateService() {
        if (mmService != null) {
            mmService!!.close()
        }
    }

    fun loginToApi() = viewModelScope.launch {
        try {
            val loginSuccess = mmService!!.loginAsync().await()
            mmLoginStateLiveData.postValue(
                    if (loginSuccess) LOGIN_SUCCESS
                    else LOGIN_FAILED
            )
        } catch (e: Exception) {
            Log.e(TAG, "Login failed", e)
            mmLoginStateLiveData.postValue(LOGIN_FAILED)
        }
    }

    fun startQueryAllStatuses() {
        queryStatusTask?.cancel()
        queryStatusTask = timerTask {
            queryAllStatuses()
        }
        queryTimer.scheduleAtFixedRate(queryStatusTask, 0, QUERY_PERIOD)
    }

    private fun queryAllStatuses() = viewModelScope.launch {
        try {
            val allProgress = mmService!!.getAllProgressAsync().await()
            mmSolveStatusLiveData.apply {
                value!!.clear()
                value!!.addAll(allProgress)
                postValue(value)
            }
            Log.d(TAG, "queryAllStatuses: result: $allProgress")
        } catch (e: Exception) {
            Log.e(TAG, "queryAllStatuses: failed", e)
        }
    }

    fun submitUnsolvedSchedule(
            request: MmProblemRequest
    ) = viewModelScope.launch {
        try {
            val solveStatus = mmService!!.solveProblemAsync(request).await()
            val message = "Solve accepted; ID: ${solveStatus.pid}"
            Log.d(TAG, message)
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Submit unsolved schedule failed", e)
        }
        queryAllStatuses()
    }

    fun getMmLoginStateLiveData(): LiveData<Int> {
        return mmLoginStateLiveData
    }

    fun getMmSolveStatusLiveData(): LiveData<ArrayList<MmSolveStatus>> {
        return mmSolveStatusLiveData
    }

    override fun onCleared() {
        invalidateService()
        queryTimer.cancel()
    }

    companion object {
        const val NOT_LOGGED_IN = 1000
        const val LOGIN_FAILED = 1001
        const val LOGIN_SUCCESS = 1002
        private const val QUERY_PERIOD: Long = 5000
    }
}