package dk.cphbusiness.coroutines.server.content

import dk.cphbusiness.coroutines.server.data.Gamer
import dk.cphbusiness.coroutines.server.data.GenerateData
import java.lang.Exception

class ClubContent : WebContent {
    var gamers = mutableListOf<Gamer>()
    val gen = GenerateData()

    init {
        gamers = gen.readGamers("gamers.txt")
        if(gamers.isEmpty()) {
            gamers = gen.generateGamers(5)
            gen.write("gamers.txt", gamers)
        }
    }

    fun getGamer(): List<Gamer> = gamers
    fun getGamer (id: Int ): Gamer? = gamers[id]
    fun putGamer ( gamer: Gamer ): Gamer {
        println("member: $gamer")
        var updatedMember = gamers[gamer.id]
        //if the member exists in database, update the member
        if (updatedMember != null){
            updatedMember.nickname = gamer.nickname
            updatedMember.score = gamer.score
            save()
            return updatedMember
        }
        //if the member doesnt exist persist it to database
        else {
            gamers[gamer.id] = gamer
            save()
            return gamer
        }
    }
    // deletes a gamer but resturns the value
    fun deleteGamer (id: Int ): Gamer {
        val g = gamers.removeAt(id)
        if (g != null) return g
        else throw Exception()
    }
    override fun save() {
        gen.write("gamers.txt", gamers)
    }
}