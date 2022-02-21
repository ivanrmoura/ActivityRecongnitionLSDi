package br.com.lsdi.activityrecongnition.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.lsdi.activityrecongnition.model.ListDetectedActivity

@Dao
interface DetectedActivityDAO {

    @Query("SELECT * FROM listdetectedactivity")
    fun getAll(): List<ListDetectedActivity>

    @Insert
    fun insertAll(vararg detectedActivity: ListDetectedActivity)

    @Delete
    fun delete(detectedActivity: ListDetectedActivity)

}