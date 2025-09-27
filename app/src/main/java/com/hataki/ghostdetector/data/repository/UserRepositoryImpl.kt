package com.hataki.ghostdetector.data.repository

import com.hataki.ghostdetector.data.model.User
import com.hataki.ghostdetector.data.remote.ApiService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {
    override suspend fun getUsers(): List<User> = api.getUsers()
}
