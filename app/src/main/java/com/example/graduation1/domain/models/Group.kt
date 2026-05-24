package com.example.graduation1.domain.models

data class Group(
    val id : String = "",
    val name : String = "",
    val image : Any = "",
    val membersCount : Int = 0,
    val members : List<String> = emptyList(),
    var onlineMembers : Int = 0
)