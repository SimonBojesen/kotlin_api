package dk.cphbusiness.coroutines.server.content

import dk.cphbusiness.coroutines.server.data.Member

class ChoirContent : WebContent {
    val members = mutableListOf<Member>()

    fun getMember(): List<Member> = members

    fun getMember(id: Int): Member? = members[id]

    fun putMember(member: Member): Member {
        println("member: $member")
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