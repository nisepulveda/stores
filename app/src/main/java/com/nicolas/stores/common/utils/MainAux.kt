package com.nicolas.stores.common.utils

import com.nicolas.stores.common.entities.StoreEntity

interface MainAux {
    fun hideFab(isVisible: Boolean = false)
    fun addStore(storeEntity: StoreEntity)
    fun updateStore(storeEntity: StoreEntity)
}