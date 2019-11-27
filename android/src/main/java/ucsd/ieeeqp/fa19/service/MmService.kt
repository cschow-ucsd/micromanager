package ucsd.ieeeqp.fa19.service

import android.content.Context
import call.*
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
        response.status == HttpStatusCode.OK
    }

    fun solveProblemAsync(
            problemRequest: MmProblemRequest
    ): Deferred<MmSolveStatus> = client.async {
        client.post<MmSolveStatus>(route("/api/op-solve"), body = problemRequest)
    }

    fun getProblemProgressAsync(
            opPIDs: OpPIDs
    ): Deferred<MmStatusResponse> = client.async {
        client.get<MmStatusResponse>(route("/api/status/ids"), body = opPIDs)
    }

    fun getAllProgressAsync(): Deferred<MmStatusResponse> = client.async {
        client.get<MmStatusResponse>(route("/api/status/all"))
    }

    fun getSolutionAsync(
            opPID: OpPID
    ): Deferred<MmSolutionResponse> = client.async {
        client.get<MmSolutionResponse>(route("/api/solution"), body = opPID)
    }

    private fun route(
            path: String
    ): String = "${config["api_url"]}/$path"

    fun close() {
        client.close()
    }
}
