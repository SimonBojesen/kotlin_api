package dk.cphbusiness.coroutines.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonWriter
import kotlin.concurrent.thread
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.coroutines.CoroutineContext

class Server(val port: Int = 4711) {
    var running = true
    val serverSocket = ServerSocket(port)

    fun handle(request: Request, response: Response) {
        println(request.resource)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val content = ChoirContent()
        //val members = content.members
        val method = request.method
        when (method) {
            Method.GET -> {
                if (request.resource != null) {
                    val result = callFunction(content, request.method, request.resource, "")
                    val memberJson = gson.toJson(result)
                    response.append(memberJson)
                    response.send()
                }
            }
            Method.PUT -> {
                val jsonBody = request.body
                println(jsonBody)
                val result = callFunction(content, request.method, request.resource, jsonBody)
                println("result: " + result.toString())
                response.append(result.toString())
                response.send()
            }
        }
    }

    fun start() {
        while (running) {
            val socket = serverSocket.accept()
            thread {
                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }
        }
    }

    fun stop() {
        running = false
        serverSocket.close()
    }

}

fun main() {
    println("Starting server")
    val server = Server()
    thread { server.start() }
    var line = readLine()
    while (line != null) {
        when (line) {
            "stop" -> server.stop()
            else -> println("czczc")
        }
        line = readLine()
    }
}