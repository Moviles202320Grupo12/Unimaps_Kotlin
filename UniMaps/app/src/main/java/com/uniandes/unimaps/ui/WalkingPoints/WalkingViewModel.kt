package com.uniandes.unimaps.ui.WalkingPoints

import androidx.lifecycle.ViewModel
import com.uniandes.unimaps.asynctasks.DBAsyncTask

class WalkingViewModel : ViewModel()  {

    private var conexionBD = DBAsyncTask();

    suspend fun updateWalkingPoints (points: Int) {
        conexionBD.updateWalkingPoints(points);
    }

}