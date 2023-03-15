package com.nicolas.stores.mainModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nicolas.stores.StoreApplication
import com.nicolas.stores.common.entities.StoreEntity

class MainInteractor {

    val getStore: LiveData<MutableList<StoreEntity>> = liveData {
        val storesLiveData = StoreApplication.database.storeDao().getAllStores()
        emitSource(storesLiveData)
    }

    suspend fun updateStore(storeEntity: StoreEntity) {
        StoreApplication.database.storeDao().updateStore(storeEntity)
    }

    suspend fun deleteStore(storeEntity: StoreEntity) {
        StoreApplication.database.storeDao().deleteStore(storeEntity)
    }
}