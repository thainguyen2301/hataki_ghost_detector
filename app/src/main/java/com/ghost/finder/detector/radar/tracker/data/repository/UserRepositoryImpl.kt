package com.ghost.finder.detector.radar.tracker.data.repository

import com.ghost.finder.detector.radar.tracker.data.model.User
import com.ghost.finder.detector.radar.tracker.data.remote.ApiService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {
    override suspend fun getUsers(): List<User> = api.getUsers()
}
