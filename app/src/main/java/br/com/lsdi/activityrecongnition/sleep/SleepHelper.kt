package br.com.lsdi.activityrecongnition.sleep

import android.app.Service
import android.util.Log
import br.com.lsdi.activityrecongnition.R
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.SleepSegmentRequest

fun Service.requestSleepUpdates(){
    val request = ActivityRecognition.getClient(this).requestSleepSegmentUpdates(
        SleepSegmentReceiver.getPendingIntent(this),
        // Registers for both SleepSegmentEvent and SleepClassifyEvent data.
        SleepSegmentRequest.getDefaultSleepSegmentRequest()
    )

    request.run {
        request.addOnSuccessListener {

            Log.d("SleepUpdate", getString(R.string.sleep_update_request_success))
        }
        request.addOnFailureListener { exception ->
            Log.d("SleepUpdate", getString(R.string.sleep_update_request_failed))
        }
    }
}

fun Service.removeSleepUpdates(){
    val request = ActivityRecognition.getClient(this).removeActivityTransitionUpdates(
        SleepSegmentReceiver.getPendingIntent(this),
    )

    request.run {
        request.addOnSuccessListener {

            Log.d("SleepUpdate", getString(R.string.sleep_update_remove_success))
        }
        request.addOnFailureListener { exception ->
            Log.d("SleepUpdate", getString(R.string.sleep_update_remove_failed))
        }
    }
}
