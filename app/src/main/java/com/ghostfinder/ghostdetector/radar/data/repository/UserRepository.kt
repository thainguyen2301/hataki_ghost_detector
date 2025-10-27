package com.ghostfinder.ghostdetector.radar.data.repository

import com.ghostfinder.ghostdetector.radar.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}
