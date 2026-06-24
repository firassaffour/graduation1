package com.example.graduation1.domain.models

data class Group(
    val id : String = "",
    val admin : String = "",
    val name : String = "",
    val image : Any = "",
    val members : List<User> = emptyList(),
    val isMember : Boolean = false
)