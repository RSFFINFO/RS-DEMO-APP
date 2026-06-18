package com.example.data.repository

import com.example.data.dao.UserDao
import com.example.data.dao.WebAppDao
import com.example.data.model.User
import com.example.data.model.WebApp
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val userDao: UserDao,
    private val webAppDao: WebAppDao
) {
    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun registerUser(user: User): Boolean {
        val existing = userDao.getUserByUsername(user.username)
        if (existing != null) return false
        userDao.registerUser(user)
        return true
    }

    fun getAppsForUser(username: String): Flow<List<WebApp>> {
        return webAppDao.getAppsForUser(username)
    }

    suspend fun insertApp(app: WebApp) {
        webAppDao.insertApp(app)
    }

    suspend fun deleteApp(app: WebApp) {
        webAppDao.deleteApp(app)
    }

    suspend fun getAppById(id: Int): WebApp? {
        return webAppDao.getAppById(id)
    }
}
