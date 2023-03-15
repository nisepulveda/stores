package com.nicolas.stores.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicolas.stores.common.entities.StoreEntity
import com.nicolas.stores.editModule.model.EditStoreInteractor
import kotlinx.coroutines.launch

class EditStoreViewModel: ViewModel() {
    private val storeSelected = MutableLiveData<StoreEntity>()
    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    private var interactor: EditStoreInteractor

    init {
        interactor = EditStoreInteractor()
    }

    fun setStoreSelected(storeEntity: StoreEntity) {
        storeSelected.value = storeEntity
    }

    fun getStoreSelected(): LiveData<StoreEntity> {
        return storeSelected
    }

    fun setShowFab(isVisible: Boolean) {
        showFab.value = isVisible
    }

    fun getShowFab(): LiveData<Boolean> {
        return showFab
    }

    fun setResult(value: Any) {
        result.value = value
    }

    fun getResult(): LiveData<Any> {
        return  result
    }

    fun saveStore(storeEntity: StoreEntity) {
        result.value = storeEntity
        viewModelScope.launch {
            interactor.saveStore(storeEntity)
        }
    }

    fun updateStore(storeEntity: StoreEntity) {
        result.value = storeEntity
        viewModelScope.launch {
            interactor.updateStore(storeEntity)
        }
    }
}