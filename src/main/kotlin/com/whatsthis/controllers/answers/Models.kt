package com.whatsthis.controllers.answers

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Answer(
        val answer: String,
        val posterId: Int,
        val askId: Int,
        val points: Int,
        val accepted: Boolean
)

class AnswerPost @JsonCreator constructor(
        @JsonProperty("answer") val answer: String,
        @JsonProperty("posterId") val posterId: Int,
        @JsonProperty("askId") val askId: Int

)