package com.ghostfinder.ghostdetector.radar.data.remote

import com.ghostfinder.ghostdetector.radar.data.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
