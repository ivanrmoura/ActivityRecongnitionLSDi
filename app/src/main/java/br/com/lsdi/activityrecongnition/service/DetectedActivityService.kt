
package br.com.lsdi.activityrecongnition.service

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import br.com.lsdi.activityrecongnition.detectedactivity.DETECTED_ACTIVITY_NOTIFICATION_ID
import br.com.lsdi.activityrecongnition.detectedactivity.removeActivityUpdates
import br.com.lsdi.activityrecongnition.detectedactivity.requestActivityUpdates
import br.com.lsdi.activityrecongnition.helper.NotificationHelper
import br.com.lsdi.activityrecongnition.sleep.removeSleepUpdates
import br.com.lsdi.activityrecongnition.sleep.requestSleepUpdates
import br.com.lsdi.activityrecongnition.transitions.removeActivityTransitionUpdates
import br.com.lsdi.activityrecongnition.transitions.requestActivityTransitionUpdates
import java.util.*

const val ACTIVITY_UPDATES_INTERVAL = 500L
private const val SERVICE_CHANNEL_ID = "service_channel_id"
const val SERVICE_NOTIFICATION_ID = 11
class DetectedActivityService : Service() {

  private val helper by lazy { NotificationHelper(this) }
  val timer = Timer()


  inner class LocalBinder : Binder() {

    val serverInstance: DetectedActivityService
      get() = this@DetectedActivityService
  }

  override fun onBind(p0: Intent?): IBinder = LocalBinder()

  override fun onCreate() {
    super.onCreate()
    requestActivityUpdates()

    //requestActivityTransitionUpdates()
    //requestSleepUpdates()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    startForeground(1, helper.getNotification())
    return START_NOT_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    stopSelf()
    timer.cancel()
    removeActivityUpdates()
    removeActivityTransitionUpdates()
    removeSleepUpdates()
    NotificationManagerCompat.from(this).cancel(DETECTED_ACTIVITY_NOTIFICATION_ID)
    Log.d("SERVICE_DESTROY", "Stop service")
  }


}