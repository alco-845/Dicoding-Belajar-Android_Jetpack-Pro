package com.alcorp.projectacademyapp.ui.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alcorp.projectacademyapp.data.AcademyRepository
import com.alcorp.projectacademyapp.data.source.local.entity.CourseEntity
import com.alcorp.projectacademyapp.vo.Resource

class AcademyViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    fun getCourses(): LiveData<Resource<List<CourseEntity>>> = academyRepository.getAllCourses()
}