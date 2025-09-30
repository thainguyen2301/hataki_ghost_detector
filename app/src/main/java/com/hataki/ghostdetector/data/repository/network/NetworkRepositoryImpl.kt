package com.hataki.ghostdetector.data.repository.network

import com.hataki.ghostdetector.data.system.network.NetworkManager
import kotlinx.coroutines.flow.Flow

class NetworkRepositoryImpl(
    private val networkManager: NetworkManager
) : NetworkRepository {
    override fun observeNetwork(): Flow<Boolean> = networkManager.isConnected

    override fun registerNetworkChange(): Result<Unit> {
        return Result.success(networkManager.register())
    }

    override fun unRegisterNetworkChange(): Result<Unit> {
        return Result.success(networkManager.unregister())
    }
}