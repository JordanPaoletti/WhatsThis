package com.whatsthis.controllers.user

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class User(
        val id: Int,
        val username: String,
        val post_points: Int,
        val answer_points: Int,
        val admin: Boolean
)

internal class UserPost @JsonCreator constructor(
        @JsonProperty("username")val username: String,
        @JsonProperty("admin")val admin: Boolean
) {

}