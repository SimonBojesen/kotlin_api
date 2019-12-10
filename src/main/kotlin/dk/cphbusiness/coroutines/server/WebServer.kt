package dk.cphbusiness.coroutines.server

import com.google.gson.GsonBuilder
import dk.cphbusiness.coroutines.server.content.ChoirContent
import dk.cphbusiness.coroutines.server.content.WebContent
import java.net.ServerSocket
import kotlin.concurrent.thread

class WebServer(val content: WebContent, val port: Int = 4711) {
    var running = true
    val serverSocket = ServerSocket(port)

    fun handle(request: Request, response: Response) {
        println(request.resource)
        val gson = GsonBuilder().setPrettyPrinting().create()
        //val members = content.members
        val method = request.method
        when (method) {
            Method.GET -> {
                if (request.resource != null) {
                    val result = request.callFunction(content, request.method, request.resource, "")
                    val memberJson = gson.toJson(result)
                    response.append(memberJson)
                    response.send()
                }
            }
            Method.PUT -> {
                val jsonBody = request.body
                val result = request.callFunction(content, request.method, request.resource, jsonBody)
                response.append(result.toString())
                response.send()
            }
            Method.DELETE -> {
                val result = request.callFunction(content, request.method, request.resource, "")
                val memberJson = gson.toJson(result)
                response.append("$memberJson has been deleted")
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

