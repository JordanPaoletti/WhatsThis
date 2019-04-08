package com.whatsthis.controllers.asks

import com.whatsthis.utils.execQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository("asksDao")
class AsksDAO {

    @Autowired
    private lateinit var dataSource: DataSource

    fun getAsk(id: Int): Ask? {
        return execQuery(
                dataSource,
                "SELECT * FROM asks WHERE id = ?",
                {stmt -> stmt.setInt(1, id)},
                {rs ->
                    if (rs.next()) {
                        Ask(
                                id = rs.getInt("id"),
                                imgLink = rs.getString("img_link"),
                                description = rs.getString("description"),
                                posterId = rs.getInt("poster_id"),
                                classId = rs.getInt("class_id"),
                                time = rs.getTimestamp("time"),
                                answered = rs.getBoolean("answered"),
                                points = rs.getInt("points")
                        )
                    } else {
                        null
                    }
                }
        )
    }

    fun getAsks(): List<Ask> {
        execQuery(
                dataSource,
                "SELECT * FROM asks",
                {},
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

    fun postAsk(ask: AskPost): Ask? {
        return execQuery(
                dataSource,
                """
                   INSERT INTO asks (img_link, description, poster_id, class_id)
                   VALUES (?, ?, ?, ?) RETURNING *
                """,
                {stmt ->
                    stmt.setString(1, ask.imgLink)
                    stmt.setString(2, ask.description)
                    stmt.setInt(3, ask.posterId)
                    stmt.setInt(4, ask.classId)
                },
                { rs ->
                    if (rs.next()) {
                        Ask(
                                id = rs.getInt("id"),
                                imgLink = rs.getString("img_link"),
                                description = rs.getString("description"),
                                posterId = rs.getInt("poster_id"),
                                classId = rs.getInt("class_id"),
                                time = rs.getTimestamp("time"),
                                answered = rs.getBoolean("answered"),
                                points = rs.getInt("points")
                        )
                    } else {
                        null
                    }
                }
        )
    }

}