package com.uniandes.unimaps.models

data class UserModel (val id: String, val fullName: String, val email: String, val phoneNo:String, val password:String, val username:String)
{
    // no-argument constructor
    constructor(): this("", "", "", "", "", "")
}