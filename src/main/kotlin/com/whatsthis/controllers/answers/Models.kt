package com.whatsthis.controllers.answers

data class Answer(
        val answer: String,
        val posterId: Int,
        val askId: Int,
        val points: Int,
        val accepted: Boolean
)