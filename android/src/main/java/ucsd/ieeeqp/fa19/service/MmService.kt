package ucsd.ieeeqp.fa19.service

import android.content.Context
import android.util.Log
import call.MmProblemRequest
import call.MmSolutionResponse
import call.MmSolveStatus
import call.MmStatusResponse
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import optaplanner.OpPID
import optaplanner.OpPIDs

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
        Log.d("MmService", "Request solve: $problemRequest")
        val response = client.post<HttpResponse> {
            url((route("/api/solve")))
            contentType(ContentType.Application.Json)
            body = problemRequest
        }
        if (response.status == HttpStatusCode.Accepted) response.receive<MmSolveStatus>()
        else throw RuntimeException("Problem solve request not accepted")
    }

    fun getProblemProgressAsync(
            opPIDs: OpPIDs
    ): Deferred<MmStatusResponse> = client.async {
        client.post<MmStatusResponse>() {
            url(route("/api/status/ids"))
            contentType(ContentType.Application.Json)
            body = opPIDs
        }
    }

    fun getAllProgressAsync(): Deferred<MmStatusResponse> = client.async {
        client.get<MmStatusResponse>(route("/api/status/all"))
    }

    fun getSolutionAsync(
            opPID: OpPID
    ): Deferred<MmSolutionResponse> = client.async {
        client.post<MmSolutionResponse> {
            url(route("/api/solution"))
            contentType(ContentType.Application.Json)
            body = opPID
        }
    }

    private fun route(
            path: String
    ): String = "${config["api_url"]}/$path"

    fun close() {
        client.close()
    }
}