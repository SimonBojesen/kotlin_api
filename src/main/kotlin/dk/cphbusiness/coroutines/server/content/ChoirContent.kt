package dk.cphbusiness.coroutines.server.content

import dk.cphbusiness.coroutines.server.data.GenerateData
import dk.cphbusiness.coroutines.server.data.Member

class ChoirContent : WebContent {
    var members = mutableMapOf<Int, Member>()
    val gen = GenerateData()

    /*init {
        members = gen.read("members.txt")
        if(gamers.isEmpty()) {
            gamers = gen.generateGamers(5)
            gen.write("gamers.txt", gamers)
        }
    }*/

    fun getMember(): List<Member> = members.values.toList()
    fun getMember(id: Int): Member? = members[id]
    fun putMember(member: Member): Member {
        var updatedMember = members[member.id]
        //if the member exists in database, update the member
        if (updatedMember != null){
            updatedMember.name = member.name
            save()
            return updatedMember
        }
        //if the member doesnt exist persist it to database
        else {
            members[member.id] = member
            save()
            return member
        }
    }

    override fun save() {
    }
}