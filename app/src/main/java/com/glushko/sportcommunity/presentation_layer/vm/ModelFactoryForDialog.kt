package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ModelFactoryForDialog(private val application: Application, private val friend_id: Long, private val type_dialog: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return DialogViewModel(application =  application, friend_id = friend_id, type_dialog = type_dialog) as T
    }
}