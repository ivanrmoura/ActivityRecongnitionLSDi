

package br.com.lsdi.activityrecongnition

import android.app.Application
import androidx.room.Room


class ActivityApplication : Application() {

  companion object {
    var database: AppDatabase? = null
  }

  override fun onCreate() {
    super.onCreate()

    ActivityApplication.database = Room.databaseBuilder(
      applicationContext,
      AppDatabase::class.java, "master-db"
    ).build()

  }
}