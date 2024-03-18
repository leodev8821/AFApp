package com.example.afapp.database

class PostModel {

    // CONSTANTS
    // If you change the database schema, you must increment the database version.
    companion object {

        const val DATABASE_NAME = "AFApp.db"
        const val DATABASE_VERSION = 1

    }

    /*
     *  Table of Users
     */
    object UserTable {
        const val TABLE_NAME = "UserTable"
        const val COLUMN_NAME_ID = "_id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_LASTNAME = "Lastname"
        const val COLUMN_EMAIL = "Email"
        const val COLUMN_PASSWORD = "Password"

        val COLUMN_NAMES = arrayOf(
            COLUMN_NAME_ID,
            COLUMN_NAME,
            COLUMN_LASTNAME,
            COLUMN_EMAIL,
            COLUMN_PASSWORD,
        )

        val SORT_ORDER = "$COLUMN_NAME_ID ASC"

        const val SQL_CREATE_TABLE_USER =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_LASTNAME TEXT, " +
                    "$COLUMN_EMAIL TEXT NOT NULL UNIQUE, " +
                    "$COLUMN_PASSWORD TEXT)"

        const val SQL_DELETE_TABLE_USER = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    /*
     *  Table of Post
     */
    object PostTable {
        const val TABLE_NAME = "PostTable"
        const val COLUMN_NAME_ID = "_id"
        const val COLUMN_TITLE = "Title"
        const val COLUMN_BODY = "Body"
        const val COLUMN_USER_POST = "UserPost"
        const val COLUMN_TAGS = "Tags"
        const val COLUMN_REACTIONS = "Reactions"
        const val COLUMN_DATE = "Date"
        const val TABLE_USER = UserTable.COLUMN_NAME_ID

        val COLUMN_NAMES = arrayOf(
            COLUMN_NAME_ID,
            COLUMN_TITLE,
            COLUMN_BODY,
            COLUMN_USER_POST,
            COLUMN_TAGS,
            COLUMN_REACTIONS,
            COLUMN_DATE,
        )

        val SORT_ORDER = "$COLUMN_NAME_ID DESC"

        const val SQL_CREATE_TABLE_POST =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT, " +
                    "$COLUMN_BODY TEXT, " +
                    "$COLUMN_USER_POST INTEGER, " +
                    "$COLUMN_TAGS TEXT, " +
                    "$COLUMN_REACTIONS INTEGER, " +
                    "$COLUMN_DATE INTEGER, " +
                    "FOREIGN KEY ($COLUMN_USER_POST)"+
                    "REFERENCES ${UserTable.TABLE_NAME} ($TABLE_USER) ON DELETE CASCADE);"

        const val SQL_DELETE_TABLE_POST = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}