package com.example.graduation1.domain.models

data class CandidateData(
    val country : String = "",
    val governorate : String = "",
    val yearsOfExperience : Int = 0,
    val preferredRole : String = "",
    val bio : String = "",
    val availabilityStatus : String = ""
)