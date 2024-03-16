package com.example.afapp.database.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager (context:Context) {

    private var sharedPref : SharedPreferences = context.getSharedPreferences("my_session", Context.MODE_PRIVATE)

    companion object {
        const val USER_LOGIN_STATE = "USER_LOGIN_STATE"
    }


    // Propiedades que se desean guardar en la Sesión
    fun setUserLoginState (isLoggedIn: Boolean){
        val editor = sharedPref.edit()
        editor.putBoolean(USER_LOGIN_STATE, isLoggedIn)
        editor.apply()
    }

    fun getUserLoginState(): Boolean{
        return sharedPref.getBoolean(USER_LOGIN_STATE, false)
    }

}