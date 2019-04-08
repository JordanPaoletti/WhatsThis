package com.whatsthis.controllers.classroom

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("classrooms")
class ClassroomController {

    @Autowired
    private lateinit var classroomDao: ClassroomDAO

    @RequestMapping(
            method = [GET],
            produces = ["application/json"],
            value = [""]
    )
    internal fun getClassroom(@RequestParam("id") id: Int): Classroom? {
        return classroomDao.getClassroom(id)
    }
}