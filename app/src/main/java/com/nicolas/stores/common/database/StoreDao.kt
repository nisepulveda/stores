package com.nicolas.stores.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nicolas.stores.common.entities.StoreEntity

@Dao
interface StoreDao {
    @Query("SELECT * FROM StoreEntity")
    fun getAllStores() : LiveData<MutableList<StoreEntity>>

    @Query("SELECT * FROM StoreEntity WHERE id = :id")
    fun getStoreById(id: Long): StoreEntity

    @Insert
    suspend fun addStore(storeEntity: StoreEntity): Long

    @Update
    suspend fun updateStore(storeEntity: StoreEntity)

    @Delete
    suspend fun deleteStore(storeEntity: StoreEntity)
}