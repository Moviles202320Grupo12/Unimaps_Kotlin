package com.uniandes.unimaps.ui.WalkingPoints

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class WalkingViewModel : ViewModel()  {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Walking Points Fragment"
    }
    val text: LiveData<String> = _text

}