package com.radityalabs.android.kotlin

import com.radityalabs.android.realm.ProductObject

/**
 * Created by radityagumay on 1/14/17.
 */

interface MainView {
    fun onResult(obj: List<ProductObject>?)
}