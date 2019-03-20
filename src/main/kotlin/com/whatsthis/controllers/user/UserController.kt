package com.whatsthis.controllers.user

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam

@RestController
class UserController {

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/user"]
    )
    internal fun getUser(@RequestParam("id") id: Int): User {
        return User(1, "bob", 12, 12, false)
    }

    @RequestMapping(
            method = [POST],
            produces = ["application/json"],
            consumes = ["application/json"],
            value = ["/user"]
    )
    internal fun postUser(@RequestBody user: UserPost): User {
        return User(
                1,
                user.username,
                0,
                0,
                user.admin
        )
    }
}