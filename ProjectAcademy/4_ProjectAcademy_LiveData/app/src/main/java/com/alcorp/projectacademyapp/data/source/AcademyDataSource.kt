package com.alcorp.projectacademyapp.data.source

import androidx.lifecycle.LiveData
import com.alcorp.projectacademyapp.data.CourseEntity
import com.alcorp.projectacademyapp.data.ModuleEntity

interface AcademyDataSource {

    fun getAllCourses() : LiveData<List<CourseEntity>>

    fun getBookmarkedCourses() : LiveData<List<CourseEntity>>

    fun getCourseWithModules(courseId: String) : LiveData<CourseEntity>

    fun getAllModulesByCourse(courseId: String) : LiveData<List<ModuleEntity>>

    fun getContent(courseId: String, moduleId: String) : LiveData<ModuleEntity>
}