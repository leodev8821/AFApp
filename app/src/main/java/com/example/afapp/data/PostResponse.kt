package com.example.afapp.data

import com.example.afapp.database.Post
import com.google.gson.annotations.SerializedName

class PostResponse (
    @SerializedName("results") val results:List<Post>
)
{

}

class Post (
    @SerializedName("id") val id:Int,
    @SerializedName("title") val title:String,
    @SerializedName("body") val body:String,
    @SerializedName("tags") val tags:List<String>,
    @SerializedName("reactions") val reactions:Int,
)
{

}