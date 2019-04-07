package com.whatsthis

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource

@RestController
class Controller {


    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var s3Client: AmazonS3Client

    @Value("\${aws.bucket-name}")
    private var bucket: String? = null



    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/tests3"]
    )
    internal fun test(): String {
        val buckets = s3Client.listBuckets().mapNotNull { it.name }.joinToString(", ")

        return """ {"buckets": "$buckets"} """
    }

    @RequestMapping(
            method = [POST],
            produces = ["application/json"],
            consumes = ["multipart/form-data"],
            value = ["/testUp"]
    )
    internal fun testUpload(@RequestParam("file") file: MultipartFile,
                            @RequestParam("posterId") posterId: Int,
                            @RequestParam("classId") classId: Int,
                            @RequestParam("description") description: String): String {
        var fileName = "${posterId}_${classId}_${System.currentTimeMillis()}${file.originalFilename}"

        val metadata = ObjectMetadata()
        metadata.contentLength = file.bytes.size.toLong()

        s3Client.putObject(PutObjectRequest(
                bucket,
                fileName,
                file.bytes.inputStream(),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead))
        return """ {"buckets": "${file.originalFilename}, $posterId, $classId, $description"} """
    }


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