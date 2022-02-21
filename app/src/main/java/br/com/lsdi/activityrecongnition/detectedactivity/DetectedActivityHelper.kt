package br.com.lsdi.activityrecongnition.detectedactivity

import android.app.Service
import android.util.Log
import br.com.lsdi.activityrecongnition.R
import br.com.lsdi.activityrecongnition.service.ACTIVITY_UPDATES_INTERVAL
import com.google.android.gms.location.ActivityRecognitionClient

fun Service.requestActivityUpdates() {
    val task = ActivityRecognitionClient(this).requestActivityUpdates(ACTIVITY_UPDATES_INTERVAL,
        DetectedActivityReceiver.getPendingIntent(this))

    task.run {
        addOnSuccessListener {
            Log.d("ActivityUpdate", getString(R.string.activity_update_request_success))
        }
        addOnFailureListener {
            Log.d("ActivityUpdate", getString(R.string.activity_update_request_failed))
        }
    }
}

fun Service.removeActivityUpdates() {
    val task = ActivityRecognitionClient(this).removeActivityUpdates(
        DetectedActivityReceiver.getPendingIntent(this))

    task.run {
        addOnSuccessListener {
            Log.d("ActivityUpdate", getString(R.string.activity_update_remove_success))
        }
        addOnFailureListener {
            Log.d("ActivityUpdate", getString(R.string.activity_update_remove_failed))
        }
    }
}