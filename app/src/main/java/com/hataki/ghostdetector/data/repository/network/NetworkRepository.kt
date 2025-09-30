package com.hataki.ghostdetector.data.repository.network

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun observeNetwork(): Flow<Boolean>

    fun registerNetworkChange(): Result<Unit>

    fun unRegisterNetworkChange(): Result<Unit>
}