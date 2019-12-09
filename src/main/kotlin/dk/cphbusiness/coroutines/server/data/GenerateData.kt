package dk.cphbusiness.coroutines.server.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.lang.StringBuilder
import kotlin.random.Random

class GenerateData {
    val nicknames = listOf<String>("Xtremekilla", "CringePwn69", "TotallyNot12", "UrMomGay", "420BlazeIt")
    val scores = listOf<Int>(1000, 2000, 3000, 4000, 5000)

    fun generateGamers(amount: Int) : MutableList<Gamer> {
        val gamers = ArrayList<Gamer>()
        for(i in 1..amount){
            val gamer = Gamer(i, nicknames.random(), scores.random())
            gamers.add(gamer)
        }
        return gamers
    }

    fun readGamers(filename: String) : MutableList<Gamer> {
        val gson = Gson()
        val path = StringBuilder()
        path.append("src/main/kotlin/dk/cphbusiness/coroutines/server/data/")
        path.append(filename)
        return if(File(path.toString()).exists()) {
            val bufferedReader: BufferedReader = File(path.toString()).bufferedReader()
            val json = bufferedReader.use { it.readText() }
            val type = object : TypeToken<List<Gamer>>() {}.type
            gson.fromJson<ArrayList<Gamer>>(json, type)
        } else {
            return mutableListOf()
        }
    }

    fun write(filename : String, list : List<Any>){
        val gson = Gson()
        val jsonString = gson.toJson(list)
        val path = StringBuilder()
        path.append("src/main/kotlin/dk/cphbusiness/coroutines/server/data/")
        path.append(filename)
        File(path.toString()).bufferedWriter().use { out -> out.write(jsonString) }
    }
}