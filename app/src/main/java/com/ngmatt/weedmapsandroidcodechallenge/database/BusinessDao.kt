package com.ngmatt.weedmapsandroidcodechallenge.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao  {
    @Query("SELECT * FROM BusinessEntity WHERE term =:term")
    fun getCachedResults(term: String) : Flow<List<BusinessEntity>>

    @Query("UPDATE BusinessEntity SET topReview = :topReview WHERE businessId = :businessId")
    suspend fun updateTopReview(businessId: String, topReview: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg business: BusinessEntity)

    @Query("DELETE FROM BusinessEntity WHERE term =:term")
    suspend fun delete(term: String)

    @Query("DELETE FROM BusinessEntity")
    suspend fun deleteAll()
}