package br.com.lsdi.activityrecongnition

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.lsdi.activityrecongnition.adapter.DetectedActivityAdapter
import br.com.lsdi.activityrecongnition.detectedactivity.DETECTED_ACTIVITY_ACTION
import br.com.lsdi.activityrecongnition.detectedactivity.DetectedActivityReceiver
import br.com.lsdi.activityrecongnition.service.DetectedActivityService
import br.com.lsdi.activityrecongnition.transitions.*
import com.google.android.gms.location.DetectedActivity
import kotlinx.android.synthetic.main.activity_main.*


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private var isTrackingStarted = false
        set(value) {
            resetBtn.visibility = if(value) View.VISIBLE else View.GONE
            field = value
        }

    private val transitionBroadcastReceiver: TransitionsReceiver = TransitionsReceiver().apply {
        action = { setDetectedActivity(it)}
    }

    private val detectedActivityReceiver: DetectedActivityReceiver = DetectedActivityReceiver().apply {
        action = { setConfidenceActivity(it)}
    }

    private fun setConfidenceActivity(activities: List<DetectedActivity>) {
        setupRecyclerView(activities)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val arrayAlunos = arrayListOf<String>("Ivan", "Rodrigues", "Moura")


        startBtn

        startBtn.setOnClickListener {
            if (isPermissionGranted()) {
                registerReceiver(detectedActivityReceiver, IntentFilter(DETECTED_ACTIVITY_ACTION))

                registerReceiver(transitionBroadcastReceiver, IntentFilter(TRANSITIONS_RECEIVER_ACTION))
                startService(Intent(this, DetectedActivityService::class.java))

                //requestActivityTransitionUpdates()
                isTrackingStarted = true
                Toast.makeText(this@MainActivity, "You've started activity tracking",
                    Toast.LENGTH_SHORT).show()
            } else {
                requestPermission()
            }
        }
        stopBtn.setOnClickListener {
            stopService(Intent(this, DetectedActivityService::class.java))
            LocalBroadcastManager.getInstance(this).unregisterReceiver(detectedActivityReceiver)
            //removeActivityTransitionUpdates()
            LocalBroadcastManager.getInstance(this).unregisterReceiver(transitionBroadcastReceiver)


            Toast.makeText(this, "You've stopped tracking your activity", Toast.LENGTH_SHORT).show()
        }
        resetBtn.setOnClickListener {
            resetTracking()
        }
    }


    private fun setupRecyclerView (activities: List<DetectedActivity>){

        rv_detected_activity.setHasFixedSize(true)
        rv_detected_activity.layoutManager = LinearLayoutManager(this)
        rv_detected_activity.itemAnimator = DefaultItemAnimator()
        rv_detected_activity.adapter = DetectedActivityAdapter(activities)
    }


    private fun resetTracking() {
        isTrackingStarted = false
        setDetectedActivity(SupportedActivity.NOT_STARTED)
        //removeActivityTransitionUpdates()
        stopService(Intent(this, DetectedActivityService::class.java))
    }

    override fun onResume() {
        super.onResume()
        //registerReceiver(transitionBroadcastReceiver, IntentFilter(TRANSITIONS_RECEIVER_ACTION))
        //registerReceiver(detectedActivityReceiver, IntentFilter(DETECTED_ACTIVITY_ACTION))
    }

    override fun onPause() {
        //unregisterReceiver(transitionBroadcastReceiver)
        //unregisterReceiver(detectedActivityReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        //removeActivityTransitionUpdates()
        //stopService(Intent(this, DetectedActivityService::class.java))
        super.onDestroy()
    }

    private fun setDetectedActivity(supportedActivity: SupportedActivity) {
        activityImage.setImageDrawable(ContextCompat.getDrawable(this, supportedActivity.activityImage))
        activityTitle.text = "You are currently ${getString(supportedActivity.activityText)}"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACTIVITY_RECOGNITION).not() &&
            grantResults.size == 1 &&
            grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showSettingsDialog(this)
        } else if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION &&
            permissions.contains(Manifest.permission.ACTIVITY_RECOGNITION) &&
            grantResults.size == 1 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("permission_result", "permission granted")
            startService(Intent(this, DetectedActivityService::class.java))
            requestActivityTransitionUpdates()
            isTrackingStarted = true
        }
    }
}