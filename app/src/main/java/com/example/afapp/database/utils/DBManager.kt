package com.example.afapp.database.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.afapp.database.PostModel

class DBManager (context:Context) :
    SQLiteOpenHelper(context, PostModel.DATABASE_NAME, null, PostModel.DATABASE_VERSION){

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        db?.execSQL("PRAGMA foreign_keys = ON;")
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(PostModel.UserTable.SQL_CREATE_TABLE_USER)
        db.execSQL(PostModel.PostTable.SQL_CREATE_TABLE_POST)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onDelete(db)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    private fun onDelete(db: SQLiteDatabase) {
        db.execSQL(PostModel.UserTable.SQL_DELETE_TABLE_USER)
        db.execSQL(PostModel.PostTable.SQL_DELETE_TABLE_POST)
    }
}