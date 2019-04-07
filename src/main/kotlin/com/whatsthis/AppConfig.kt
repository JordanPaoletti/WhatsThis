package com.whatsthis

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.Region
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI
import java.sql.SQLException
import javax.sql.DataSource

@Configuration
open class AppConfig {

    @Value("\${aws.access-key-id}")
    private var access: String? = null

    @Value("\${aws.secret-access-key}")
    private var secret: String? = null

    /*
    @Value("\${aws.bucket-name}")
    private var bucket: String? = null
    */

    @Bean
    open fun awsS3Client(): AmazonS3Client {
        return AmazonS3Client(BasicAWSCredentials(access, secret))
    }


    @Value("\${spring.datasource.url}")
    private var dbUrl: String? = null

    @Bean
    @Throws(SQLException::class)
    open fun dataSource(): DataSource {
        return if (dbUrl?.isEmpty() != false) {
            val dbUri = URI(System.getenv("DATABASE_URL"))
            val username = dbUri.userInfo.split(":")[0]
            val password = dbUri.userInfo.split(":")[1]
            val url = "jdbc:postgresql://" + dbUri.host +
                    ':' + dbUri.port + dbUri.path + "?sslmode=require"

            val config = HikariConfig()
            config.username = username
            config.password = password
            config.jdbcUrl = url
            HikariDataSource(config)
        } else {
            val config = HikariConfig()
            config.jdbcUrl = dbUrl
            HikariDataSource(config)
        }
    }
}