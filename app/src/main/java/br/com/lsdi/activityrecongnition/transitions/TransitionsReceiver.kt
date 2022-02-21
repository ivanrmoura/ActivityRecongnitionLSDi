
package br.com.lsdi.activityrecongnition.transitions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.lsdi.activityrecongnition.*
import br.com.lsdi.activityrecongnition.dao.DetectedActivityDAO
import br.com.lsdi.activityrecongnition.dao.TransitionActivityDAO
import br.com.lsdi.activityrecongnition.model.ListDetectedActivity
import br.com.lsdi.activityrecongnition.model.ListTransitionActivity
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import com.google.gson.Gson
import java.time.LocalDateTime
import java.util.concurrent.Executors

const val TRANSITIONS_RECEIVER_ACTION = "${BuildConfig.APPLICATION_ID}_transitions_receiver_action"


private const val TRANSITION_PENDING_INTENT_REQUEST_CODE = 200

private const val TRANSITION_ACTIVITY_CHANNEL_ID = "transition_activity_channel_id"
const val TRANSITION_ACTIVITY_NOTIFICATION_ID = 110

@RequiresApi(Build.VERSION_CODES.O)
class TransitionsReceiver: BroadcastReceiver() {

  var action: ((SupportedActivity) -> Unit)? = null
  private var appDatabase: AppDatabase
  private var transitionActivityDAO: TransitionActivityDAO

  private var executor = Executors.newSingleThreadExecutor()
  private val gson = Gson()

  companion object {

    fun getPendingIntent(context: Context): PendingIntent {
      val intent = Intent(TRANSITIONS_RECEIVER_ACTION)
      return PendingIntent.getBroadcast(context, TRANSITION_PENDING_INTENT_REQUEST_CODE, intent,
        PendingIntent.FLAG_UPDATE_CURRENT)
    }
  }

  init {
    appDatabase = ActivityApplication.database!!
    transitionActivityDAO = appDatabase.listTransitionActivity()
  }

  override fun onReceive(context: Context, intent: Intent) {
    // 1
    if (ActivityTransitionResult.hasResult(intent)) {
      // 2
      val result = ActivityTransitionResult.extractResult(intent)
      result?.let { handleTransitionEvents(it.transitionEvents, context) }
    }
  }

  private fun handleTransitionEvents(transitionEvents: List<ActivityTransitionEvent>,  context: Context) {
    val enter = R.string.enter
    val exit = R.string.exit

    transitionEvents
      .filter { it.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER }
      .forEach {
        showNotification(SupportedActivity.fromActivityType(it.activityType), context)
        action?.invoke(SupportedActivity.fromActivityType(it.activityType))
        executor.execute {
          saveTransictionActivity(it, context, enter)
        }
      }
    // showNotification(SupportedActivity.fromActivityType(it.activityType), context)}

    transitionEvents
      .filter { it.transitionType == ActivityTransition.ACTIVITY_TRANSITION_EXIT }
      .forEach {
        executor.execute {
          saveTransictionActivity(it, context, exit)
        }
      }

    executor.execute{
      Log.d("DATABASE", gson.toJson(transitionActivityDAO.getAll()))
    }

  }


  private fun saveTransictionActivity(activityTransitionEvent: ActivityTransitionEvent,
                                      context: Context,
                                      transitionText: Int){
    val datetime = LocalDateTime.now().toString()
    val activityType = context.getString(SupportedActivity.fromActivityType(activityTransitionEvent.activityType).activityText)
    val transitionType = context.getString(transitionText)
    val listTransitionActivity = ListTransitionActivity(datetime, activityType, transitionType)
    transitionActivityDAO.insertAll(listTransitionActivity)
  }

  private fun showNotification(supportedActivity: SupportedActivity, context: Context) {
    createNotificationChannel(context)
    val intent = Intent(context, MainActivity::class.java).apply {
      putExtra(SUPPORTED_ACTIVITY_KEY, supportedActivity)
    }


    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
      PendingIntent.FLAG_UPDATE_CURRENT)

    val activityText = context.getString(supportedActivity.activityText)
    //val transition = SupportedActivity.fromActivityType(activitytTransitionEvent.transitionType)

    val builder = NotificationCompat.Builder(context, TRANSITION_ACTIVITY_CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setContentTitle("Transition API")
      .setContentText(activityText)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent(pendingIntent)
      .setOnlyAlertOnce(true)
      .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
      notify(TRANSITION_ACTIVITY_NOTIFICATION_ID, builder.build())
    }
  }

  private fun createNotificationChannel(context: Context) {
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name = "detected_activity_channel_name"
      val descriptionText = "detected_activity_channel_description"
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(TRANSITION_ACTIVITY_CHANNEL_ID, name, importance).apply {
        description = descriptionText
        enableVibration(false)
      }
      // Register the channel with the system
      val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }
}
