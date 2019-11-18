package ucsd.ieeeqp.fa19.service


import android.content.Context
import android.util.Log
import call.MmPIDs
import call.MmProblemRequest
import call.MmSolveAccepted
import call.MmStatusResponse
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class MmService(
        context: Context,
        serverAuthCode: String?
) {

    private val config = LocalhostConfig(context)
    private val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private val client = MmHttpClient.create(
            serverAuthCode = serverAuthCode,
            sessionAuthHeader = preferences.getString(MmHttpClient.MICROMANAGER_SESSION, null)
    ) { session ->
        val editor = preferences.edit()
        editor.putString(MmHttpClient.MICROMANAGER_SESSION, session)
        editor.apply()
    }

    fun loginAsync(): Deferred<Boolean> = client.async {
        val response = client.get<HttpResponse>(route("/api/login"))
        Log.d("MmService", response.status.toString())
        response.status == HttpStatusCode.OK
    }

    fun postProblemAsync(
            problemRequest: MmProblemRequest
    ): Deferred<MmSolveAccepted> = client.async {
        client.post<MmSolveAccepted>(route("/api/op-solve"), body = problemRequest)
    }

    fun getProblemProgressAsync(
            mmPIDs: MmPIDs
    ): Deferred<MmStatusResponse> = client.async {
        client.get<MmStatusResponse>(route("/api/status"), body = mmPIDs)
    }

    private fun route(
            path: String
    ): String = "${config["api_url"]}/$path"

    fun close() {
        client.close()
    }
}
