package com.whatsthis.controllers.classroom

import com.whatsthis.controllers.user.User
import com.whatsthis.utils.execQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.sql.DataSource

@Repository("classroomDao")
class ClassroomDAO {

    @Autowired
    private lateinit var dataSource: DataSource

    fun getClassroom(id: Int): Classroom?
            = execQuery(
            dataSource,
            """
                    SELECT * FROM classroom_users cu
                        join users u on cu.user_id = u.id
                        join classroom c on cu.class_id = c.id
                        where cu.class_id = ?
                """.trimIndent(),
            {stmt -> stmt.setInt(1, id)},
            {rs ->
                if (rs.next()) {
                    val name = rs.getString("name")
                    val users = ArrayList<User>()
                    users.add(userFromRS(rs))

                    while (rs.next()) {
                        users.add(userFromRS(rs))
                    }

                    return Classroom(
                            id = id,
                            name = name,
                            users = users
                    )
                } else null

            }
    )
}

/* rs.next() must be called before this function */
private fun userFromRS(rs: ResultSet): User {
    return User(
                id = rs.getInt("id"),
                username = rs.getString("username"),
                postPoints = rs.getInt("post_points"),
                answerPoints = rs.getInt("answer_points"),
                admin = rs.getBoolean("admin")
        )
}
