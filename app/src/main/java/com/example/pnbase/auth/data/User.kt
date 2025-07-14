package com.example.pnbase.auth.data

data class User(
    val name: String,
    val email: String,
    val status: Status
)

sealed class Status(val displayStatus: String){
    object Admin: Status("admin")
    object Users: Status("user")
}
