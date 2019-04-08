package com.whatsthis.controllers.asks

import java.sql.Timestamp

data class Ask(
        val id: Int,
        val imgLink: String,
        val description: String,
        val posterId: Int,
        val classId: Int,
        val time: Timestamp,
        val answered: Boolean,
        val points: Int
)

data class AskPost(
        val imgLink: String,
        val description: String,
        val posterId: Int,
        val classId: Int
)