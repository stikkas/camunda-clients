package ru.tinkoff.rr.clients.worker

import java.util.Scanner
import ru.tinkoff.rr.clients.enums.Mode

interface Worker {
    suspend fun work()

    companion object {
        fun fromMode(mode: Mode?): Worker = when (mode) {
            Mode.INCIDENTS -> {
                val (rootUrl, processDefinitionId) = getParams(arrayOf("rootUrl", "process definition id"))
                IncidentsWorker(rootUrl, processDefinitionId)
            }

            Mode.MANUAL -> ManualWorker()
            else -> throw IllegalArgumentException("$mode - unknown mode")
        }

        private fun getParams(params: Array<String>): Array<String> {
            val scanner = Scanner(System.`in`)
            val result = Array(params.size) { "" }
            params.forEachIndexed { i, it ->
                print("$it: ")
                result[i] = scanner.next().trim()
            }
            return result
        }
    }
}
