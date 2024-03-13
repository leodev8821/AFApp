package com.example.afapp.database

class Post (var id:Int, var title:String, var body:String, var userPost:Int, var tags:String, var reactions:Int, var date:Long) {

    override fun toString(): String {
        return "Id: $id -> Title: $title, Body: $body, UserPost: $userPost, Tags: $tags, Reactions: $reactions, Date: $date"
    }
}