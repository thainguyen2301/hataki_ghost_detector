package com.hataki.ghostdetector.data.remote

import com.hataki.ghostdetector.data.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
