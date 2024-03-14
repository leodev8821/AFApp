package com.example.afapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostServiceAPI {

    @GET("posts/")
    suspend fun getAll(
    ): Response<PostResponse>

    @GET("search")
    suspend fun searchByWord(
        @Query("word") query:String
    ): Response<PostResponse>

    @GET("user/{id}")
    suspend fun searchById(
        @Path("id") identifier:String
    ): Response<PostResponse>
}