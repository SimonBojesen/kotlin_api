package dk.cphbusiness.coroutines.server

import dk.cphbusiness.coroutines.server.content.ClubContent

//You should be able to start the server with any class implementing the WebContent interface
fun main () {

    val content = ClubContent (/* filename , ... */)
    val server = WebServer (content,4711)
    server.start()

    //or ChoirContent (/* filename , ... */ ). publish (4711)
}