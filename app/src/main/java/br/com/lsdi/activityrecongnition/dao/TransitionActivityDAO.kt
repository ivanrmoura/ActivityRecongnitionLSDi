package br.com.lsdi.activityrecongnition.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.lsdi.activityrecongnition.model.ListDetectedActivity
import br.com.lsdi.activityrecongnition.model.ListTransitionActivity

@Dao
interface TransitionActivityDAO {

    @Query("SELECT * FROM listtransitionactivity")
    fun getAll(): List<ListTransitionActivity>

    @Insert
    fun insertAll(vararg transitionActivity: ListTransitionActivity)

    @Delete
    fun delete(transitionActivity: ListTransitionActivity)

}