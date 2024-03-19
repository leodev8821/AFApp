package com.example.afapp.database.providers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.afapp.database.Post
import com.example.afapp.database.PostModel
import com.example.afapp.database.User
import com.example.afapp.database.utils.DBManager

class PostDAO (context: Context) {

    private val dbManager: DBManager = DBManager(context)

    fun insert(post: Post): Post {

        // Gets the data repository in write mode
        val db = dbManager.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(PostModel.PostTable.COLUMN_TITLE, post.title)
            put(PostModel.PostTable.COLUMN_BODY, post.body)
            put(PostModel.PostTable.COLUMN_USER_POST, post.userPost)
            put(PostModel.PostTable.COLUMN_TAGS, post.tags)
            put(PostModel.PostTable.COLUMN_REACTIONS, post.reactions)
            put(PostModel.PostTable.COLUMN_DATE, post.date)
            put(PostModel.PostTable.COLUMN_LIKE, post.like)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(PostModel.PostTable.TABLE_NAME, null, values)
        Log.i("DATABASE", "New record id: $newRowId")

        db.close()

        post.id = newRowId.toInt()

        return post

    }

    fun update(post: Post){

        val db = dbManager.writableDatabase

        val values = ContentValues().apply {
            put(PostModel.PostTable.COLUMN_TITLE, post.title)
            put(PostModel.PostTable.COLUMN_BODY, post.body)
            put(PostModel.PostTable.COLUMN_USER_POST, post.userPost)
            put(PostModel.PostTable.COLUMN_TAGS, post.tags)
            put(PostModel.PostTable.COLUMN_REACTIONS, post.reactions)
            put(PostModel.PostTable.COLUMN_DATE, post.date)
            put(PostModel.PostTable.COLUMN_LIKE, post.like)
        }

        val updatedRows = db.update(PostModel.PostTable.TABLE_NAME, values, "${PostModel.PostTable.COLUMN_NAME_ID} = ${post.id}", null)
        Log.i("DATABASE", "Updated $updatedRows record, " +
                "\nid: ${post.id} with Title: ${post.title}, " +
                "\nBody: ${post.body}, " +
                "\nUserId: ${post.userPost}, " +
                "\nTags: ${post.tags}, " +
                "\nReactions: ${post.reactions} " +
                "\nDate: ${post.date}" +
                "\nLike: ${post.like}"
        )

        db.close()
    }

    fun delete(post: Post){
        val db = dbManager.writableDatabase

        val deleteRows = db.delete(PostModel.PostTable.TABLE_NAME, "${PostModel.PostTable.COLUMN_NAME_ID} = ${post.id}", null)
        Log.i("DATABASE", "Deleted rows: $deleteRows")

        db.close()
    }

    @SuppressLint("Range")
    fun find(id:Int): Post? {

        val db = dbManager.writableDatabase

        val cursor = db.query(
            PostModel.PostTable.TABLE_NAME,                         // The Table to query
            PostModel.PostTable.COLUMN_NAMES,                        // The array of columns to return (pass null to get all)
            "${PostModel.PostTable.COLUMN_NAME_ID} = $id",   // The columns for the WHERE clause
            null,                                      // The values for the WHERE clause
            null,                                          // don't group the rows
            null,                                           // don't filter by row groups
            null                                           // The sort order
        )

        var post: Post? = null

        if (cursor.moveToNext()){
            val postId = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_NAME_ID))
            val postTitle = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_TITLE))
            val postBody = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_BODY))
            val postUserId = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_USER_POST))
            val postTags = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_TAGS))
            val postReactions = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_REACTIONS))
            val postDate = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_DATE)).toLong()
            val postLike = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_LIKE)) == 1

            // Every post is added to a list
            post = Post(postId, postTitle, postBody, postUserId, postTags, postReactions, postDate, postLike)
        }
        cursor.close()
        db.close()

        return post

    }

    @SuppressLint("Range")
    fun findAll(): List<Post>{

        val db = dbManager.writableDatabase

        val cursor = db.query(
            PostModel.PostTable.TABLE_NAME,                       // The Table to query
            PostModel.PostTable.COLUMN_NAMES,                     // The array of columns to return (pass null to get all)
            null,                                         // The columns for the WHERE clause
            null,                                      // The values for the WHERE clause
            null,                                          // don't group the rows
            null,                                           // don't filter by row groups
            PostModel.PostTable.SORT_ORDER                        // The sort order
        )

        val list:MutableList<Post> = mutableListOf()

        while (cursor.moveToNext()){
            val postId = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_NAME_ID))
            val postTitle = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_TITLE))
            val postBody = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_BODY))
            val postUserId = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_USER_POST))
            val postTags = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_TAGS))
            val postReactions = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_REACTIONS))
            val postDate = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_DATE)).toLong()
            val postLike = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_LIKE)) == 1

            // Every post is added to a list
            val post = Post(postId, postTitle, postBody, postUserId, postTags, postReactions, postDate, postLike)
            list.add(post)
        }
        cursor.close()
        db.close()

        return list

    }

    @SuppressLint("Range")
    fun findByTitle(title:String): Post? {

        val db = dbManager.writableDatabase

        val cursor = db.query(
            PostModel.PostTable.TABLE_NAME,                         // The Table to query
            PostModel.PostTable.COLUMN_NAMES,                        // The array of columns to return (pass null to get all)
            "${PostModel.PostTable.COLUMN_TITLE} = $title",   // The columns for the WHERE clause
            null,                                      // The values for the WHERE clause
            null,                                          // don't group the rows
            null,                                           // don't filter by row groups
            null                                           // The sort order
        )

        var post: Post? = null

        if (cursor.moveToNext()){
            val postId = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_NAME_ID))
            val postTitle = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_TITLE))
            val postBody = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_BODY))
            val postUserId = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_USER_POST))
            val postTags = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_TAGS))
            val postReactions = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_REACTIONS))
            val postDate = cursor.getString(cursor.getColumnIndex(PostModel.PostTable.COLUMN_DATE)).toLong()
            val postLike = cursor.getInt(cursor.getColumnIndex(PostModel.PostTable.COLUMN_LIKE)) == 1

            // Every post is added to a list
            post = Post(postId, postTitle, postBody, postUserId, postTags, postReactions, postDate, postLike)
        }
        cursor.close()
        db.close()

        return post

    }
}