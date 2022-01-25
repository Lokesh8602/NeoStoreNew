package com.example.neostorelokesh.dataclass

data class LoginErrorResponse(
    val message: String,
    val status: Int,
    val user_msg: String
)