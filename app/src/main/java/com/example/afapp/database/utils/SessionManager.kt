package com.example.afapp.database.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager (context:Context) {

    companion object {
        val LOGGED_USER = "LOGGED_USER"
    }

    private var sharedPref : SharedPreferences? = null

    init{
        sharedPref = context.getSharedPreferences("my_session", Context.MODE_PRIVATE)
    }

    // Propiedades que se desean guardar en la Sesión
    fun setLoggedUser (email: String){
        val editor = sharedPref?.edit()
        if (editor != null){
            editor.putString(LOGGED_USER, email)
            editor.apply()
        }
    }

    fun closeLoggedUser(){
        sharedPref?.edit()?.remove(LOGGED_USER)?.apply()
    }

    fun getLoggedUser() : String?{
        return sharedPref?.getString(LOGGED_USER, null).toString()
    }

}