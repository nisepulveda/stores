package com.nicolas.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicolas.stores.StoreApplication
import com.nicolas.stores.common.entities.StoreEntity
import com.nicolas.stores.mainModule.model.MainInteractor
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue

class MainViewModel: ViewModel() {

    private var interactor: MainInteractor

    init {
        interactor = MainInteractor()
    }

    private val stores = interactor.getStore

    fun getStores(): LiveData<MutableList<StoreEntity>> {
        return stores
    }

    fun updateStore(storeEntity: StoreEntity) {
        viewModelScope.launch {
            storeEntity.isFavorite = !storeEntity.isFavorite
            interactor.updateStore(storeEntity)
        }
    }

    fun deleteStore(storeEntity: StoreEntity) {
        viewModelScope.launch {
            interactor.deleteStore(storeEntity)
        }
    }
}