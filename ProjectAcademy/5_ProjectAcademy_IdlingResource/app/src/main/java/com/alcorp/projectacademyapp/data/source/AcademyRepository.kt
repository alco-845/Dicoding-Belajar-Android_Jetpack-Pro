package com.alcorp.projectacademyapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alcorp.projectacademyapp.data.ContentEntity
import com.alcorp.projectacademyapp.data.CourseEntity
import com.alcorp.projectacademyapp.data.ModuleEntity
import com.alcorp.projectacademyapp.data.source.remote.RemoteDataSource
import com.alcorp.projectacademyapp.data.source.remote.RemoteDataSource.*
import com.alcorp.projectacademyapp.data.source.remote.response.ContentResponse
import com.alcorp.projectacademyapp.data.source.remote.response.CourseResponse
import com.alcorp.projectacademyapp.data.source.remote.response.ModuleResponse

class AcademyRepository private constructor(private val remoteDataSource: RemoteDataSource) : AcademyDataSource {

    companion object {
        @Volatile
        private var instance: AcademyRepository? = null

        fun getInstance(remoteData: RemoteDataSource) : AcademyRepository =
            instance ?: synchronized(this) {
                instance ?: AcademyRepository(remoteData)
            }
    }

    override fun getAllCourses(): LiveData<List<CourseEntity>> {
        val courseResult = MutableLiveData<List<CourseEntity>>()
        remoteDataSource.getAllCourses(object : LoadCourseCallback{
            override fun onAllCoursesReceived(courseResponses: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in courseResponses) {
                    val course = CourseEntity(response.id,
                            response.title,
                            response.description,
                            response.date,
                            false,
                            response.imagePath)
                    courseList.add(course)
                }
                courseResult.postValue(courseList)
            }
        })

        return courseResult
    }

    override fun getBookmarkedCourses(): LiveData<List<CourseEntity>> {
        val courseResult = MutableLiveData<List<CourseEntity>>()

        remoteDataSource.getAllCourses(object : LoadCourseCallback {
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in courseResponse) {
                    val course = CourseEntity(
                            response.id,
                            response.title,
                            response.description,
                            response.date,
                            false,
                            response.imagePath
                    )
                    courseList.add(course)
                }
                courseResult.postValue(courseList)
            }
        })
        return courseResult
    }

    override fun getCourseWithModules(courseId: String): LiveData<CourseEntity> {
        val courseResult = MutableLiveData<CourseEntity>()

        remoteDataSource.getAllCourses(object : LoadCourseCallback {
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                lateinit var course: CourseEntity
                for(response in courseResponse) {
                    if (response.id == courseId) {
                        course = CourseEntity(
                                response.id,
                                response.title,
                                response.description,
                                response.date,
                                false,
                                response.imagePath
                        )
                    }
                }
                courseResult.postValue(course)
            }
        })
        return courseResult
    }

    override fun getAllModulesByCourse(courseId: String): LiveData<List<ModuleEntity>> {
        val modulesResults = MutableLiveData<List<ModuleEntity>>()

        remoteDataSource.getModules(courseId, object : LoadModulesCallback {
            override fun onAllModulesReceived(moduleResponse: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (response in moduleResponse) {
                    val course = ModuleEntity(
                            response.moduleId,
                            response.courseId,
                            response.title,
                            response.position,
                            false
                    )
                    moduleList.add(course)
                }
                modulesResults.postValue(moduleList)
            }
        })

        return modulesResults
    }

    override fun getContent(courseId: String, moduleId: String): LiveData<ModuleEntity> {
        val moduleResult = MutableLiveData<ModuleEntity>()

        remoteDataSource.getModules(courseId, object : LoadModulesCallback {
            override fun onAllModulesReceived(moduleResponse: List<ModuleResponse>) {
                lateinit var module: ModuleEntity
                for (response in moduleResponse) {
                    if (response.moduleId == moduleId) {
                        module = ModuleEntity(
                                response.moduleId,
                                response.courseId,
                                response.title,
                                response.position,
                                false
                        )
                        remoteDataSource.getContent(moduleId, object : LoadContentCallback {
                            override fun onContentReceived(contentResponse: ContentResponse) {
                                module.contentEntity = ContentEntity(contentResponse.content)
                                moduleResult.postValue(module)
                            }
                        })
                        break
                    }
                }
            }
        })

        return moduleResult
    }

}