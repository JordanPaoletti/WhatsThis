package com.whatsthis.controllers.answers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("answers")
class AnswerController {

    @Autowired
    private lateinit var answerDAO: AnswerDAO

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/ask"]
    )
    internal fun getAnswersForAsk(@RequestParam("id") askId: Int): List<Answer> {
        return answerDAO.getAskAnswers(askId)
    }

    @RequestMapping(
            method = [POST],
            produces = ["application/json"],
            consumes = ["application/json"],
            value = [""]
    )
    internal fun postUser(@RequestBody answer: AnswerPost) {
        return answerDAO.answerAsk(answer)
    }
}
