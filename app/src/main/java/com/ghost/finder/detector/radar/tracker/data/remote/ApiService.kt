package com.ghost.finder.detector.radar.tracker.data.remote

import com.ghost.finder.detector.radar.tracker.data.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
