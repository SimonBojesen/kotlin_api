package dk.cphbusiness.coroutines.server

import com.google.gson.GsonBuilder
import dk.cphbusiness.coroutines.server.data.Member
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.StringBuilder
import java.util.ArrayList
import kotlin.reflect.full.declaredFunctions

enum class Method { GET, PUT, POST, DELETE, NONHTTP }

class Protocol {
}

class Request(input: InputStream, val content: Any) {
    val resource: String
    val method: Method
    val body: String
    val headers = mutableMapOf<String, String>()

    init {
        var line = input.readLine()
        val parts = line.split(" ")
        if (parts.size != 3) {
            resource = ""
            method = Method.NONHTTP
        }
        else {
            resource = parts[1]
            method = Method.valueOf(parts[0])
        }

        line = input.readLine()
        while (line.isNotEmpty()) {
            val headerParts = line.split(":")
            headers[headerParts[0].trim()] = headerParts[1].trim()
            line = input.readLine()
        }

        val contentL = headers["Content-Length"]
        if (contentL == null) body = ""
        else body = input.readString(contentL.toInt())
    }

    fun callFunction(content: Any, method: Method, resource: String, body: String): Any? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        var parts : List<String> = ArrayList()
        if (body == "" || body == null) parts = resource.split("/" + body).filter { !it.isEmpty() }
        else parts = resource.split("/").filter { !it.isEmpty() } + body
        println("parts: " + parts)
        if (parts.size == 0) return null

        val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
        println(methodName)
        val type = content::class
        val function = type.declaredFunctions
            .filter { it.name == methodName }
            .filter { it.parameters.size == parts.size }
            .firstOrNull()
        if (function == null) return null
        if (function.parameters.size > 1) {
            val p = function.parameters[1]
            println("type-classifier: " + p.type.classifier)
            when (p.type.classifier) {
                Int::class -> {
                    val v1 = parts[1].toInt()
                    return function.call(content, v1)
                }
                else -> {
                    println("body: " + body)
                    val member = gson.fromJson(body, Member::class.java)
                    return function.call(content, member)
                }
            }
        } else return function.call(content)
    }
}

class Response(private val output: OutputStream) {
    val body = StringBuilder()

    fun append(text: String) {
        body.append(text)
    }

    fun send() {
        val head = """
        HTTP/1.1 200 OK
        Content-Type: text/html; charset=UTF-8
        Content-Length: ${body.length}
        Connection: close
        
        """.trimIndent()
        val writer = output.bufferedWriter()
        writer.append(head)
        writer.newLine()
        writer.append(body)
        writer.close()
    }

}
/*
fun main() {
  val output = ByteArrayOutputStream(1024)
  val writer = output.bufferedWriter()
  }
 */