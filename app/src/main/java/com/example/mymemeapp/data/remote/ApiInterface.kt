package com.example.mymemeapp.data.remote

import com.example.mymemeapp.models.AllMems
import retrofit2.Response
import retrofit2.http.GET


interface ApiInterface {
    @GET("get_memes")
    suspend fun getAllMemes(): Response<AllMems>
}