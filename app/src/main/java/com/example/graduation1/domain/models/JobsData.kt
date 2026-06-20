package com.example.graduation1.domain.models

data class JobsData(
    val title : String = "",
    val description : String = "",
    val location : String = "",
    val experienceLevel : String = "",
    val requiredSkills : List<String>
)