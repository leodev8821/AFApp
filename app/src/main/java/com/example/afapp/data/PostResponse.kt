package com.example.afapp.data

import com.google.gson.annotations.SerializedName

class PostResponse (
    @SerializedName("posts") val posts:List<PostItemResponse>
)
{

}

class PostItemResponse (
    @SerializedName("id") val id:Int,
    @SerializedName("title") val title:String,
    @SerializedName("body") val body:String,
    @SerializedName("tags") val tags:List<String>,
    @SerializedName("reactions") val reactions:Int,
)
{

}