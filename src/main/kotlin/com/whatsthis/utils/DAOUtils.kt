package com.whatsthis.utils

import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

inline fun <T> execQuery(
        ds: DataSource,
        sql: String,
        setParams: (PreparedStatement) -> Unit,
        fromRS: (ResultSet) -> T
): T {
    val connection = ds.connection
    try {
        val stmt = connection.prepareStatement(sql)
        setParams(stmt)

        val rs = stmt.executeQuery()
        val result = fromRS(rs)

        connection.close()
        return result
    } catch (e: Exception) {
        connection.close()
        throw e
    }
}

inline fun execStatement(
        ds: DataSource,
        sql: String,
        setParams: (PreparedStatement) -> Unit
) {
    val connection = ds.connection
    try {
        val stmt = connection.prepareStatement(sql)
        setParams(stmt)

        stmt.execute()

        connection.close()
    } catch (e: Exception) {
        connection.close()
        throw e
    }
}
