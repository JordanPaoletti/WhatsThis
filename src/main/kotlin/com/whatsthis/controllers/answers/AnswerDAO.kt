package com.whatsthis.controllers.answers

import com.whatsthis.utils.execQuery
import com.whatsthis.utils.execStatement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository("answerDao")
class AnswerDAO {

    @Autowired
    private lateinit var dataSource: DataSource

    fun getAskAnswers(id: Int): List<Answer> {
        execQuery(
                dataSource,
                "SELECT * FROM answers WHERE ask_id = ?",
                {stmt -> stmt.setInt(1, id)},
                {rs ->
                    val answers = ArrayList<Answer>()
                    while (rs.next()) {
                        answers.add(Answer(
                                answer = rs.getString("answer"),
                                posterId = rs.getInt("poster_id"),
                                askId = rs.getInt("ask_id"),
                                points =  rs.getInt("points"),
                                accepted = rs.getBoolean("accepted")
                        ))
                    }
                    return answers
                }
        )
    }

    fun answerAsk(answer: AnswerPost) {
        execStatement(
                dataSource,
                """
                    INSERT INTO answers (answer, poster_id, ask_id)
                    VALUES (?, ?, ?)
                """
        ) { stmt ->
            stmt.setString(1, answer.answer)
            stmt.setInt(2, answer.posterId)
            stmt.setInt(3, answer.askId)
        }
    }
}