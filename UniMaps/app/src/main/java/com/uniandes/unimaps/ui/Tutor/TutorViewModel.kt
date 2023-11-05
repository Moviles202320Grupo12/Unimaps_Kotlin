package com.uniandes.unimaps.ui.Tutor

import android.util.ArrayMap
import androidx.lifecycle.ViewModel
import com.uniandes.unimaps.asynctasks.DBAsyncTask

class TutorViewModel : ViewModel(){

    private var conexionBD = DBAsyncTask();

    suspend fun getDBTutors (): ArrayMap<String, Tutor> {
        return  conexionBD.getTutorCollection();
    }
}