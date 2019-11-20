package dk.cphbusiness.coroutines.server

import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.coroutines.CoroutineContext

class Server2(val port: Int) : CoroutineScope {
  private val job = Job()

  fun close() {
    job.cancel()
    }

  suspend fun handle(socket: Socket) = withContext(Dispatchers.Default) {
    val input = socket.getInputStream().bufferedReader()
    val message = input.readLine()
    println(message)

    socket.getOutputStream().bufferedWriter().use { output ->
      output.write("""
          HTTP/1.1 200 OK
          Content-Type: text/html; charset=UTF-8
          Content-Length: 138

          <html>
            <head>
              <title>An Example Page</title>
            </head>
            <body>
              <p>Hello World, this is a very simple HTML document.</p>
            </body>
          </html>
          """.trimIndent())
      output.flush()
      }
    }


  fun serve() {
    val serverSocket = ServerSocket(port)
    while (job.isActive) {
      val socket = serverSocket.accept()
      launch {
        handle(socket)
        }
      }
    }

  override val coroutineContext: CoroutineContext
    get() = job

  }

/*fun main() {
  println("Starting server")
  val server = Server2(4711)
  server.serve()

  val requestText = """
    GET /resource HTTP/1.1
    
    
    """.trimIndent()
  val bytes = requestText.byteInputStream()
  val bin = bytes.bufferedReader()
  println(bin.readLine())

  }*/