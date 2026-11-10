package com.survivalcoding.day18_design_principles.model

import java.time.LocalDateTime

data class Notification(
    val id: Int,
    val type: NotificationType,
    val message: String,
    val isRead: Boolean,
    val createdAt: LocalDateTime,
)

enum class NotificationType {
    NEW_RECIPE,
    SAVE_RECIPE,
}
