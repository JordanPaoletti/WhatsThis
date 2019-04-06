package com.whatsthis.controllers.classroom

import com.whatsthis.controllers.user.User

data class Classroom(
        val id: Int,
        val name: String,
        val users: List<User>
)