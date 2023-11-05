package com.uniandes.unimaps.models

data class UserModel (var id: String?, val fullName: String, val email: String, val phoneNo:String, val password:String, val username:String)
{
    // no-argument constructor
    constructor(): this("", "", "", "", "", "")
}