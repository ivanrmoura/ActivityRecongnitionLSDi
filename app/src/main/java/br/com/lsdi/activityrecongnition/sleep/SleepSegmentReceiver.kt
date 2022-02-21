package br.com.lsdi.activityrecongnition.sleep

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import br.com.lsdi.activityrecongnition.BuildConfig
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent
import com.google.gson.Gson
import java.util.concurrent.Executors

private const val SLEEP_PENDING_INTENT_REQUEST_CODE = 120
const val SLEEP_SEGMENT_ACTION = "${BuildConfig.APPLICATION_ID}_sleep_segment_receiver_action"

@RequiresApi(Build.VERSION_CODES.O)
class SleepSegmentReceiver : BroadcastReceiver() {

    private var executor = Executors.newSingleThreadExecutor()
    private val gson = Gson()


    companion object {
        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(SLEEP_SEGMENT_ACTION)
            return PendingIntent.getBroadcast(context, SLEEP_PENDING_INTENT_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SleepSegmentEvent.hasEvents (intent)) {
            val result =  SleepSegmentEvent.extractEvents(intent)
            result.let {
                handleSleepSegmentEvents(it, context)
            }
        }

        if (SleepClassifyEvent.hasEvents (intent)) {
            val result = SleepClassifyEvent.extractEvents(intent)
            result.let {
                handleSleepClassifyEvents(it, context)
            }
        }

    }

    private fun handleSleepSegmentEvents(events: List<SleepSegmentEvent>, context: Context) {
        for (event in events) {
            Log.d("SLEEP_EVENT",
                "${event.startTimeMillis} to ${event.endTimeMillis} with status ${event.status} duration: ${event.segmentDurationMillis}")
        }
    }

    private fun handleSleepClassifyEvents(events: List<SleepClassifyEvent>, context: Context) {
        for (event in events) {
            Log.d(
                "SLEEP_EVENT",
                "Confidence: ${event.confidence} - Light: ${event.light} - Motion: ${event.motion} timestampMillis: ${event.timestampMillis}")
        }
    }


}