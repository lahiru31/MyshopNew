package com.khadar3344.myshop.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.khadar3344.myshop.data.network.repository.NetworkRepository
import com.khadar3344.myshop.notifications.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncDataWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var networkRepository: NetworkRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("SyncDataWorker", "Starting sync operation")
            
            // Perform background sync operations
            val products = networkRepository.getProductsList()
            
            // Show notification on successful sync
            notificationHelper.showNotification(
                "Sync Complete",
                "Products data has been updated successfully",
                SYNC_NOTIFICATION_ID
            )
            
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncDataWorker", "Error during sync", e)
            Result.retry()
        }
    }

    companion object {
        private const val SYNC_NOTIFICATION_ID = 100
    }
}
