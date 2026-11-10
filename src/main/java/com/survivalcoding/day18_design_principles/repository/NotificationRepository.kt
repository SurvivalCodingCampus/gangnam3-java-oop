package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.Notification

interface NotificationRepository {

    suspend fun getNotifications(userId: Int): Result<List<Notification>, NetworkError>

    suspend fun markAsRead(notificationId: Int): Result<Unit, NetworkError>
}
