package dk.cphbusiness.coroutines.server

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberFunctions
import dk.cphbusiness.coroutines.server.Server as s
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dk.cphbusiness.coroutines.server.content.WebContent
import dk.cphbusiness.coroutines.server.data.Member
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.util.ArrayList

fun listFunctions(content: Any) {
    val contentType = content::class
    println(contentType.simpleName)
    contentType.memberFunctions.forEach {
        println(it)
    }
}



/*fun main() {
    val content = ClubContent()
    println(content.getGamer())
    content.deleteGamer(1)
    println(content.getGamer())
}*/

