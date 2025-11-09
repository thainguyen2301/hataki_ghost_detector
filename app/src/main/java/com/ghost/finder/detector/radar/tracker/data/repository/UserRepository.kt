package com.ghost.finder.detector.radar.tracker.data.repository

import com.ghost.finder.detector.radar.tracker.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}
