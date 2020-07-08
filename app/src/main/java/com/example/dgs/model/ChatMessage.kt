package com.aspl.chat.models

/**
 * Created by ansh on 28/08/18.
 */
class ChatMessage(
        val id: String,
        val text: String,
        val fromId: String,
        val toId: String,
        val advertisement_id: String,
        val timestamp: Long,
        val seenStatus:String
        /*val imgurl: String?*/
){
    constructor() : this("", "", "", "","", -1,"false")
}