package com.example.afapp.database.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager (context:Context) {

    companion object {
        val LOGGED_USER = -1
    }

    private var sharedPref : SharedPreferences? = null

    init{
        sharedPref = context.getSharedPreferences("my_session", Context.MODE_PRIVATE)
    }

    // Propiedades que se desean guardar en la Sesión
    fun setLoggedUser (id: Int){
        val editor = sharedPref?.edit()
        if (editor != null){
            editor.putInt(LOGGED_USER.toString(), id)
            editor.apply()
        }
    }

    fun getLoggedUser() : Int?{
        return sharedPref?.getInt(LOGGED_USER.toString(), -1)
    }

/*
    fun setFavoriteZodiac (id : String){
        val editor = sharedPref?.edit()
        if(editor != null){
            editor.putString(FAVORITE_ZODIAC, id)
            editor.apply()
        }
    }

    fun getFavoriteZodiac () : String? {
        return sharedPref?.getString(FAVORITE_ZODIAC, null)
    }*/
}