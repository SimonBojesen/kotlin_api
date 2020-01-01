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

    fun generateGamers(amount: Int) : MutableMap<Int, Gamer> {
        val gamers: MutableMap<Int, Gamer> = mutableMapOf()
        for(i in 0..amount){
            val gamer = Gamer(i, nicknames.random(), scores.random())
            gamers[i] = gamer
        }
        return gamers
    }

    fun readGamers(filename: String) : MutableMap<Int, Gamer> {
        val gson = Gson()
        val path = StringBuilder()
        path.append("src/main/kotlin/dk/cphbusiness/coroutines/server/data/")
        path.append(filename)
        //If file exists reads the contents of the file
        if(File(path.toString()).exists()) {
            val bufferedReader: BufferedReader = File(path.toString()).bufferedReader()
            val json = bufferedReader.use { it.readText() }
            val type = object : TypeToken<MutableMap<Int, Gamer>>() {}.type
            return if (json.isNullOrBlank()) mutableMapOf()
            else gson.fromJson(json, type)
        }
        else {
            return mutableMapOf()
        }
    }


    fun write(filename : String, map : MutableMap<Int, Gamer>){
        val gson = Gson()
        val jsonString = gson.toJson(map)
        val path = StringBuilder()
        path.append("src/main/kotlin/dk/cphbusiness/coroutines/server/data/")
        path.append(filename)
        File(path.toString()).bufferedWriter().use { out -> out.write(jsonString) }
    }


}