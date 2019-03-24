package com.whatsthis

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource

@RestController
class Controller {


    @Autowired
    private lateinit var dataSource: DataSource


    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/"]

    )
    internal fun root(): String {
        return """ {"test": "hello, world"} """
    }

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/db"]

    )
    internal fun db(): List<String>? {
        val connection = dataSource.connection
        try {
            val stmt = connection.createStatement()
            val rs = stmt.executeQuery("SELECT * FROM test")

            val output = ArrayList<String>()
            while (rs.next()) {
                output.add("Read from DB: " + rs.getInt("test1"))
                output.add("Read from DB: " + rs.getString("test2"))
            }

            connection.close()
            return output
        } catch (e: Exception) {
            connection.close()
            return null
        }

    }

}