package com.example.afapp.database

class User (var id:Int, var name:String, var lastname:String, var email:String, var password:String) {

    override fun toString(): String {
        return "Id: $id -> Name: $name, Lastname: $lastname, Email: $email, Password: $password"
    }
}