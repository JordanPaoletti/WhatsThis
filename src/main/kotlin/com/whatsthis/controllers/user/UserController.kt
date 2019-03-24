package com.whatsthis.controllers.user

import com.whatsthis.controllers.answers.Answer
import com.whatsthis.controllers.asks.Ask
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("users")
class UserController {

    @Autowired
    private lateinit var userDao: UserDAO

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = [""]
    )
    internal fun getUser(@RequestParam("id") id: Int): User? {
        return userDao.getUser(id)
    }

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/asks"]
    )
    internal fun getUserAsks(@RequestParam("id") id: Int): List<Ask> {
        return userDao.getUserAsks(id)
    }

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/answers"]
    )
    internal fun getUserAnswers(@RequestParam("id") id: Int): List<Answer> {
        return userDao.getUserAnswers(id)
    }

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/notifications"]
    )
    internal fun getUserNotifications(@RequestParam("id") id: Int): List<Notification> {
        return userDao.getUserNotifications(id)
    }

    @RequestMapping(
            method = [POST],
            produces = ["application/json"],
            consumes = ["application/json"],
            value = [""]
    )
    internal fun postUser(@RequestBody user: UserPost): User?{
        return userDao.createUser(user)
    }

    @RequestMapping(
            method = [POST],
            value = ["/vote"]
    )
    internal fun postVote(@RequestParam("userId") userId: Int,
                          @RequestParam("askId") askId: Int) {
        userDao.userFollowAsk(userId, askId)
    }

    @RequestMapping(
            method = [PUT],
            value = ["/notifications"]
    )
    internal fun putReadNotification(@RequestParam("userId") userId: Int,
                                     @RequestParam("noteId") noteId: Int) {
        userDao.setReadNotification(userId, noteId)
    }
}