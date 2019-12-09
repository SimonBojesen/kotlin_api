package dk.cphbusiness.coroutines.server

import dk.cphbusiness.coroutines.server.content.WebContent
import java.net.ServerSocket

class WebServer(val content: WebContent, val port: Int = 4711) {
    var running = true
    val serverSocket = ServerSocket(port)

    fun handle(request: Request, response: Response) {

    }
    fun start() {}
    fun stop() {
        TODO("Implement stop")
    }
}