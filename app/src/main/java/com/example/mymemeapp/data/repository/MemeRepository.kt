package com.example.mymemeapp.data.repository

import com.example.mymemeapp.data.remote.ApiInterface
import javax.inject.Inject

class MemeRepository @Inject
            constructor(private val apiInterface: ApiInterface) {

    suspend fun getAllMemes() = apiInterface.getAllMemes()
}