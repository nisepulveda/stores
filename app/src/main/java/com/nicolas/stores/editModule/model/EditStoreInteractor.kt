package com.nicolas.stores.editModule.model

import androidx.lifecycle.LiveData
import com.nicolas.stores.StoreApplication
import com.nicolas.stores.common.entities.StoreEntity

class EditStoreInteractor {

    suspend fun saveStore(storeEntity: StoreEntity) {
        StoreApplication.database.storeDao().addStore(storeEntity)
    }

    suspend fun updateStore(storeEntity: StoreEntity) {
        StoreApplication.database.storeDao().updateStore(storeEntity)
    }
}