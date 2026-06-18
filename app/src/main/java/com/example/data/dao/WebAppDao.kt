package com.example.data.dao

import androidx.room.*
import com.example.data.model.WebApp
import kotlinx.coroutines.flow.Flow

@Dao
interface WebAppDao {
    @Query("SELECT * FROM web_apps WHERE creatorUsername = :username ORDER BY createdAt DESC")
    fun getAppsForUser(username: String): Flow<List<WebApp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: WebApp)

    @Delete
    suspend fun deleteApp(app: WebApp)

    @Query("SELECT * FROM web_apps WHERE id = :id LIMIT 1")
    suspend fun getAppById(id: Int): WebApp?
}
