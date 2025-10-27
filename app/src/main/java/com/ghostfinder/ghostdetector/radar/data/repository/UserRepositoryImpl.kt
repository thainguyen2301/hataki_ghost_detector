package com.ghostfinder.ghostdetector.radar.data.repository

import com.ghostfinder.ghostdetector.radar.data.model.User
import com.ghostfinder.ghostdetector.radar.data.remote.ApiService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {
    override suspend fun getUsers(): List<User> = api.getUsers()
}
