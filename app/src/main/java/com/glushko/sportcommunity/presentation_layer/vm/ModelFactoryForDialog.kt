package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ModelFactoryForDialog(private val application: Application, private val friend_id: Long) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return DialogViewModel(application =  application, friend_id = friend_id) as T
    }
}