package ru.tinkoff.rr.clients

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import java.util.Scanner
import ru.tinkoff.rr.clients.enums.Mode
import ru.tinkoff.rr.clients.worker.Worker

suspend fun main(args: Array<String>) {
    val mode = if (args.isEmpty()) {
        println(
            """
            |Usage: ./clients [MODE]
            |Where MODE is one of: 
            ${Mode.values().map { "|   $it" }.joinToString("\n")}
        """.trimMargin()
        )

        print("What mode do you want to launch: ")
        Scanner(System.`in`).next()
    } else {
        args[0]
    }

    Worker.fromMode(Mode.fromString(mode)).work()
}

suspend fun exec(block: suspend HttpClient.() -> Unit) {
    val client = HttpClient(CIO) { install(ContentNegotiation) { json() } }
    client.block()
    client.close()
}


