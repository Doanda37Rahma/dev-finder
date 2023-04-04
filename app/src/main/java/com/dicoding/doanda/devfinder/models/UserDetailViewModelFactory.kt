package com.dicoding.doanda.devfinder.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class UserDetailViewModelFactory(
    private val application: Application,
    private val argUserName: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, String::class.java)
            .newInstance(application, argUserName)
    }


}