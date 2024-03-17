package com.example.afapp.database.providers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.afapp.database.PostModel
import com.example.afapp.database.User
import com.example.afapp.database.utils.DBManager

class UserDAO (context:Context) {

    private val dbManager: DBManager = DBManager(context)

    fun insert(user: User): User {

        // Gets the data repository in write mode
        val db = dbManager.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(PostModel.UserTable.COLUMN_NAME, user.name)
            put(PostModel.UserTable.COLUMN_LASTNAME, user.lastname)
            put(PostModel.UserTable.COLUMN_EMAIL, user.email)
            put(PostModel.UserTable.COLUMN_PASSWORD, user.password)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(PostModel.UserTable.TABLE_NAME, null, values)

        db.close()

        user.id = newRowId.toInt()

        Log.i("DATABASE", "New  $newRowId record, " +
                "Id: $newRowId " +
                "\nName: ${user.name}, " +
                "\nLastname: ${user.lastname}, " +
                "\nEmail: ${user.email}, " +
                "\nPassword: ${user.password}")

        return user

    }

    fun update(user: User){

        val db = dbManager.writableDatabase

        val values = ContentValues().apply {
            put(PostModel.UserTable.COLUMN_NAME, user.name)
            put(PostModel.UserTable.COLUMN_LASTNAME, user.lastname)
            put(PostModel.UserTable.COLUMN_EMAIL, user.email)
            put(PostModel.UserTable.COLUMN_PASSWORD, user.password)
        }

        val updatedRows = db.update(PostModel.UserTable.TABLE_NAME, values, "${PostModel.UserTable.COLUMN_NAME_ID} = ${user.id}", null)
        Log.i("DATABASE", "Updated $updatedRows record, " +
                "Id: ${user.id} " +
                "\nName: ${user.name}, " +
                "\nLastname: ${user.lastname}, " +
                "\nEmail: ${user.email}, " +
                "\nPassword: ${user.password}")

        db.close()

    }

    fun delete(user: User){
        val db = dbManager.writableDatabase

        val deleteRows = db.delete(PostModel.UserTable.TABLE_NAME, "${PostModel.UserTable.COLUMN_NAME_ID} = ${user.id}", null)
        Log.i("DATABASE", "Deleted rows: $deleteRows")

        db.close()
    }

    @SuppressLint("Range")
    fun find(email:String?): User? {

        val db = dbManager.writableDatabase

        val cursor = db.query(
            PostModel.UserTable.TABLE_NAME,                         // The Table to query
            PostModel.UserTable.COLUMN_NAMES,                        // The array of columns to return (pass null to get all)
            "${PostModel.UserTable.COLUMN_EMAIL} = $email",   // The columns for the WHERE clause
            null,                                      // The values for the WHERE clause
            null,                                          // don't group the rows
            null,                                           // don't filter by row groups
            null                                           // The sort order
        )

        var user: User? = null

        if (cursor.moveToNext()){
            val userId = cursor.getInt(cursor.getColumnIndex(PostModel.UserTable.COLUMN_NAME_ID))
            val userName = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_NAME))
            val userLastname = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_LASTNAME))
            val userEmail = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_EMAIL))
            val userPassword = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_PASSWORD))

            // Every post is added to a list
            user = User(userId, userName, userLastname, userEmail, userPassword)
        }
        cursor.close()
        db.close()

        return user
    }

    @SuppressLint("Range")
    fun findAll(): List<User>{

        val db = dbManager.writableDatabase

        val cursor = db.query(
            PostModel.UserTable.TABLE_NAME,                       // The Table to query
            PostModel.UserTable.COLUMN_NAMES,                     // The array of columns to return (pass null to get all)
            null,                                         // The columns for the WHERE clause
            null,                                      // The values for the WHERE clause
            null,                                          // don't group the rows
            null,                                           // don't filter by row groups
            PostModel.UserTable.SORT_ORDER                        // The sort order
        )

        val list:MutableList<User> = mutableListOf()

        while (cursor.moveToNext()){
            val userId = cursor.getInt(cursor.getColumnIndex(PostModel.UserTable.COLUMN_NAME_ID))
            val userName = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_NAME))
            val userLastname = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_LASTNAME))
            val userEmail = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_EMAIL))
            val userPassword = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_PASSWORD))

            // Every post is added to a list
            val user = User(userId, userName, userLastname, userEmail, userPassword)
            list.add(user)
        }
        cursor.close()
        db.close()

        return list
    }

    @SuppressLint("Range")
    fun findByEmailPass(email:String, pass:String): User? {

        val db = dbManager.writableDatabase

        val cursor = db.query(
            PostModel.UserTable.TABLE_NAME,                         // The Table to query
            PostModel.UserTable.COLUMN_NAMES,                        // The array of columns to return (pass null to get all)
            "${PostModel.UserTable.COLUMN_EMAIL} = '$email' AND ${PostModel.UserTable.COLUMN_PASSWORD} = '$pass'",   // The columns for the WHERE clause
            null,                                      // The values for the WHERE clause
            null,                                          // don't group the rows
            null,                                           // don't filter by row groups
            null                                           // The sort order
        )

        var user: User? = null

        if (cursor.moveToNext()){
            val userId = cursor.getInt(cursor.getColumnIndex(PostModel.UserTable.COLUMN_NAME_ID))
            val userName = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_NAME))
            val userLastname = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_LASTNAME))
            val userEmail = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_EMAIL))
            val userPassword = cursor.getString(cursor.getColumnIndex(PostModel.UserTable.COLUMN_PASSWORD))

            // Every post is added to a list
            user = User(userId, userName, userLastname, userEmail, userPassword)
        }
        cursor.close()
        db.close()

        return user
    }
}