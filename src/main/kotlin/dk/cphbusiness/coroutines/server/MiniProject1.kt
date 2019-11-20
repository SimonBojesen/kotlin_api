package dk.cphbusiness.coroutines.server

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberFunctions
import dk.cphbusiness.coroutines.server.Server as s
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.util.ArrayList

interface WebContent {
    fun save()
}

class WebServer(val content: WebContent, val port: Int = 80) {
    fun start() {}
    fun stop() {
        TODO("Implement stop")
    }
}


data class Member(val id: Int, val name: String)

class ChoirContent : WebContent {
    val members = mutableMapOf<Int, Member>(
        7 to Member(7, "Kurt"),
        17 to Member(17, "Sonja")
    )

    fun getMember(): List<Member> = members.values.toList()

    fun getMember(id: Int): Member? = members[id]

    fun putMember(member: Member): Member {
        if (member != null) {
            members.put(member.id, member)
        }
        return member
    }

    // ...
    override fun save() {
        val gson = Gson()
        //val bufferedReader: BufferedReader = File().bufferedReader()
    }
}

fun listFunctions(content: Any) {
    val contentType = content::class
    println(contentType.simpleName)
    contentType.memberFunctions.forEach {
        println(it)
    }
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

/*fun main() {
    val content = ChoirContent()
    println(callFunction(content, Method.GET, "/member/7"))

    val server = WebServer(content, 4711)
    server.start()

}*/

