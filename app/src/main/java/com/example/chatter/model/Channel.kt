package com.example.chatter.model

data class Channel(
    val id: String = "",
    val name: String = "",
    val cratedAt :Long =  System.currentTimeMillis()
){}
