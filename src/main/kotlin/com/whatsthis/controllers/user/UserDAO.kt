package com.whatsthis.controllers.user

import com.whatsthis.controllers.answers.Answer
import com.whatsthis.controllers.asks.Ask
import com.whatsthis.utils.execQuery
import com.whatsthis.utils.execStatement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.sql.DataSource

@Repository("userDao")
class UserDAO {

    @Autowired
    private lateinit var dataSource: DataSource

    fun getUser(id: Int): User? {
        return execQuery(
                dataSource,
                "SELECT * FROM users WHERE id = ?",
                {stmt -> stmt.setInt(1, id)},
                ::userFromRS
        )
    }

    fun getUserAsks(id: Int): List<Ask> {
        execQuery(
                dataSource,
                "SELECT * FROM asks WHERE poster_id = ?",
                {stmt -> stmt.setInt(1, id)},
                {rs ->
                    val asks = ArrayList<Ask>()
                    while (rs.next()) {
                        asks.add(Ask(
                                id = rs.getInt("id"),
                                imgLink = rs.getString("img_link"),
                                description = rs.getString("description"),
                                posterId = rs.getInt("poster_id"),
                                classId = rs.getInt("class_id"),
                                time = rs.getTimestamp("time"),
                                answered = rs.getBoolean("answered"),
                                points = rs.getInt("points")
                        ))
                    }
                    return asks
                }
        )
    }

    fun getUserAnswers(id: Int): List<Answer> {
        execQuery(
                dataSource,
                "SELECT * FROM answers WHERE poster_id = ?",
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

    fun getUserNotifications(id: Int): List<Notification> {
        execQuery(
                dataSource,
                "SELECT * FROM user_notifications WHERE user_id = ?",
                {stmt -> stmt.setInt(1, id)},
                {rs ->
                    val notifications = ArrayList<Notification>()
                    while (rs.next()) {
                        notifications.add(Notification(
                                id = rs.getInt("id"),
                                typeId = rs.getInt("type_id"),
                                read = rs.getBoolean("read"),
                                time = rs.getTimestamp("time"),
                                causeId = rs.getInt("cause_id"),
                                linkId = rs.getInt("link_id"),
                                userId = rs.getInt("user_id")
                        ))
                    }
                    return notifications
                }
        )
    }

    internal fun createUser(user: UserPost): User? {
        return execQuery(
                dataSource,
                "INSERT INTO users (username, admin) VALUES (?, ?) RETURNING *",
                {stmt ->
                    stmt.setString(1, user.username)
                    stmt.setBoolean(2, user.admin)
                },
                ::userFromRS
        )
    }

    fun userFollowAsk(userId: Int, askId: Int) {
        execStatement(
                dataSource,
                "SELECT user_vote_ask(?, ?)"
        ) { stmt ->
            stmt.setInt(1, userId)
            stmt.setInt(2, askId)
        }
    }

    fun setReadNotification(userId: Int, noteId: Int) {
        execStatement(
                dataSource,
                "UPDATE user_notifications SET read = true WHERE user_id = ? AND id = ?"
        ) { stmt ->
            stmt.setInt(1, userId)
            stmt.setInt(2, noteId)
        }
    }
}

private fun userFromRS(rs: ResultSet): User? {
    return if (rs.next()) {
        User(
                id = rs.getInt("id"),
                username = rs.getString("username"),
                postPoints = rs.getInt("post_points"),
                answerPoints = rs.getInt("answer_points"),
                admin = rs.getBoolean("admin")
        )

    } else {
        null
    }
}