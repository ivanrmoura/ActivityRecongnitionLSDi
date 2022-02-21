
package br.com.lsdi.activityrecongnition.transitions

import android.app.Activity
import android.app.Service
import android.util.Log
import br.com.lsdi.activityrecongnition.R
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity


fun Activity.requestActivityTransitionUpdates() {
  val request = ActivityTransitionRequest(getActivitiesToTrack())
  val task = ActivityRecognitionClient(this).requestActivityTransitionUpdates(request,
      TransitionsReceiver.getPendingIntent(this))

  task.run {
    addOnSuccessListener {
      Log.d("TransitionUpdate", getString(R.string.transition_update_request_success))
    }
    addOnFailureListener {
      Log.d("TransitionUpdate", getString(R.string.transition_update_request_failed))
    }
  }
}


fun Activity.removeActivityTransitionUpdates() {
  val task = ActivityRecognitionClient(this).removeActivityTransitionUpdates(
      TransitionsReceiver.getPendingIntent(this))

  task.run {
    addOnSuccessListener {
      Log.d("TransitionUpdate", getString(R.string.transition_update_remove_success))
    }
    addOnFailureListener {
      Log.d("TransitionUpdate", getString(R.string.transition_update_remove_failed))
    }
  }
}


fun Service.requestActivityTransitionUpdates() {
    val request = ActivityTransitionRequest(getActivitiesToTrack())
    val task = ActivityRecognitionClient(this).requestActivityTransitionUpdates(request,
        TransitionsReceiver.getPendingIntent(this))

    task.run {
        addOnSuccessListener {
            Log.d("TransitionUpdate", getString(R.string.transition_update_request_success))
        }
        addOnFailureListener {
            Log.d("TransitionUpdate", getString(R.string.transition_update_request_failed))
        }
    }
}


fun Service.removeActivityTransitionUpdates() {
    val task = ActivityRecognitionClient(this).removeActivityTransitionUpdates(
        TransitionsReceiver.getPendingIntent(this))

    task.run {
        addOnSuccessListener {
            Log.d("TransitionUpdate", getString(R.string.transition_update_remove_success))
        }
        addOnFailureListener {
            Log.d("TransitionUpdate", getString(R.string.transition_update_remove_failed))
        }
    }
}


private fun getActivitiesToTrack(): List<ActivityTransition> =
    mutableListOf<ActivityTransition>()
        .apply {
          add(ActivityTransition.Builder()
              .setActivityType(DetectedActivity.STILL)
              .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
              .build())
          add(ActivityTransition.Builder()
              .setActivityType(DetectedActivity.STILL)
              .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
              .build())
          add(ActivityTransition.Builder()
              .setActivityType(DetectedActivity.WALKING)
              .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
              .build())
          add(ActivityTransition.Builder()
              .setActivityType(DetectedActivity.WALKING)
              .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
              .build())
          add(ActivityTransition.Builder()
              .setActivityType(DetectedActivity.RUNNING)
              .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
              .build())
          add(ActivityTransition.Builder()
              .setActivityType(DetectedActivity.RUNNING)
              .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
              .build())
        }