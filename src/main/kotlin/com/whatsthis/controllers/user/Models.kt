package com.whatsthis.controllers.user

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.IllegalArgumentException
import java.sql.Timestamp

enum class NotificationType(val id: Int) {
    ASK_POINTS(1),
    ASK_ANSWERED(2),
    ANSWER_ACCEPTED(3),
    ANSWER_POINTS(4),
    ANSWER_REPLY(5)

}

fun notificationTypeFromId(id: Int)
    = when(id) {
    1 -> NotificationType.ASK_POINTS
    2 -> NotificationType.ASK_ANSWERED
    3 -> NotificationType.ANSWER_ACCEPTED
    4 -> NotificationType.ANSWER_POINTS
    5 -> NotificationType.ANSWER_REPLY
    else -> throw IllegalArgumentException("Invalid type id")
}

data class Notification(
        val id: Int,
        val typeId: Int,
        val read: Boolean,
        val time: Timestamp,
        val causeId: Int,
        val linkId: Int,
        val userId: Int
)

data class User(
        val id: Int,
        val username: String,
        val postPoints: Int,
        val answerPoints: Int,
        val admin: Boolean
)

internal class UserPost @JsonCreator constructor(
        @JsonProperty("username")val username: String,
        @JsonProperty("admin")val admin: Boolean
) {

}