package com.uniandes.unimaps.ui.Events

import android.util.ArrayMap
import androidx.lifecycle.ViewModel
import com.uniandes.unimaps.asynctasks.DBAsyncTask

class ObjectViewModel :ViewModel(){

    private var conexionBD = DBAsyncTask();

     suspend fun getDBEvents (): ArrayMap<String, Event> {
        return  conexionBD.getEventsCollection();
    }
}