package ru.tinkoff.rr.clients.worker

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.tinkoff.rr.clients.dto.CamundaJob
import ru.tinkoff.rr.clients.dto.Count
import ru.tinkoff.rr.clients.dto.JobRetry
import ru.tinkoff.rr.clients.exec

class IncidentsWorker(private val rootUrl: String, private val processDefinitionId: String) : Worker {
    override suspend fun work() {
        exec {
            val response =
                get("${rootUrl}job/count?suspended=false&noRetriesLeft=true&processDefinitionId=$processDefinitionId")
            val count: Count = response.body()
            println(count)

            if (count.count > 0) {
                setRetries(rootUrl, processDefinitionId, 0, count.count)
            }
        }
    }


    private suspend fun setRetries(rootUrl: String, processDefinitionId: String, offset: Int, max: Int) {
        exec {
            val limit = 200
            val nomyjob = Regex("(no.*?found)|(does not exist)", RegexOption.IGNORE_CASE)
            val response = get(
                "${rootUrl}job?suspended=false&processDefinitionId=$processDefinitionId&noRetriesLeft=true&maxResults=$limit&firstResultl=$offset"
            )
            val data: List<CamundaJob> = response.body()
            val newOffset = offset + data.size
            val myJobs = data.filter { it -> it.exceptionMessage?.let { !nomyjob.matches(it) } ?: true }.map { it.id }
            println("${myJobs.size} jobs")

            if (myJobs.isNotEmpty()) {
                val res = post("${rootUrl}job/retries") {
                    contentType(ContentType.Application.Json)
                    setBody(JobRetry(1, myJobs))
                }
                println("Set retries status - ${res.status}")
                if (newOffset < max) {
                    setRetries(rootUrl, processDefinitionId, newOffset, max)
                }
            }
        }
    }

}
