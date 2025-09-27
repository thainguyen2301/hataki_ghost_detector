package com.hataki.ghostdetector.data.repository

import com.hataki.ghostdetector.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}
