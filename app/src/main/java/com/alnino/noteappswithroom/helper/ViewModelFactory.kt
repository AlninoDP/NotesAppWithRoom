package com.alnino.noteappswithroom.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alnino.noteappswithroom.ui.insert.NoteAddUpdateActivity
import com.alnino.noteappswithroom.ui.insert.NoteAddUpdateViewModel
import com.alnino.noteappswithroom.ui.main.MainViewModel
import java.lang.IllegalArgumentException

/// This Class is used for adding context when calling
/// ViewModel class in Activity
class ViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {

        companion object {
            @Volatile
            private var INSTANCE:ViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application):ViewModelFactory{
                if (INSTANCE == null){
                    synchronized(ViewModelFactory::class.java){
                        INSTANCE = ViewModelFactory(application)
                    }
                }
                return INSTANCE as ViewModelFactory
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(NoteAddUpdateViewModel::class.java)){
            return NoteAddUpdateViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}