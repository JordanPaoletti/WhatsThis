package com.whatsthis.controllers.asks

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("asks")
class AsksController {

    @Autowired
    private lateinit var asksDao: AsksDAO

    @Autowired
    private lateinit var s3Client: AmazonS3Client

    @Value("\${aws.bucket-name}")
    private var bucket: String? = null

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = [""]
    )
    internal fun getAsk(@RequestParam("id") id: Int): Ask? {
        return asksDao.getAsk(id)
    }

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = ["/all"]
    )
    internal fun getAsks(): List<Ask> {
        return asksDao.getAsks()
    }

    @RequestMapping(
            method = [POST],
            produces = ["application/json"],
            consumes = ["multipart/form-data"],
            value = ["/testUp"]
    )
    internal fun postAsk(
            @RequestParam("file") file: MultipartFile,
            @RequestParam("posterId") posterId: Int,
            @RequestParam("classId") classId: Int,
            @RequestParam("description") description: String
    ): Ask? {
        val fileName = "${posterId}_${classId}_${System.currentTimeMillis()}${file.originalFilename}"
        val url = "https://s3.us-east-2.amazonaws.com/$bucket/$fileName"

        val metadata = ObjectMetadata()
        metadata.contentLength = file.bytes.size.toLong()

        s3Client.putObject(PutObjectRequest(
                bucket,
                fileName,
                file.bytes.inputStream(),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead))

        return asksDao.postAsk(AskPost(
                imgLink = url,
                description = description,
                posterId = posterId,
                classId = classId
        ))
    }
}