package br.com.lsdi.activityrecongnition

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.lsdi.activityrecongnition.dao.DetectedActivityDAO
import br.com.lsdi.activityrecongnition.dao.TransitionActivityDAO
import br.com.lsdi.activityrecongnition.model.ListDetectedActivity
import br.com.lsdi.activityrecongnition.model.ListTransitionActivity

@Database(entities = arrayOf(ListDetectedActivity::class,ListTransitionActivity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

  abstract fun listDetectedActivityDao(): DetectedActivityDAO
  abstract fun listTransitionActivity(): TransitionActivityDAO
}