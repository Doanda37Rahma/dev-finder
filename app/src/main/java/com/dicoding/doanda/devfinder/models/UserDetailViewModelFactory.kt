package com.dicoding.doanda.devfinder.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class UserDetailViewModelFactory(private val argUserName: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(String::class.java)
            .newInstance(argUserName)
    }


}