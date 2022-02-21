
package br.com.lsdi.activityrecongnition

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.gms.location.DetectedActivity
import java.lang.IllegalArgumentException

const val SUPPORTED_ACTIVITY_KEY = "activity_key"

enum class SupportedActivity(
    @DrawableRes val activityImage: Int,
    @StringRes val activityText: Int
) {

  NOT_STARTED(R.drawable.time_to_start, R.string.time_to_start),
  STILL(R.drawable.ic_still, R.string.still),
  WALKING(R.drawable.ic_walking, R.string.walking),
  RUNNING(R.drawable.ic_running, R.string.running),
  IN_VEHICLE(R.drawable.ic_driving,R.string.in_vehicle),
  ON_BICYCLE(R.drawable.ic_on_bicycle, R.string.on_bicycle),
  ON_FOOT(R.drawable.ic_walking, R.string.on_foot),
  UNKNOWN(R.drawable.ic_unknown, R.string.unknown),
  TILTING(R.drawable.ic_tilting, R.string.tilting);


  companion object {

    fun fromActivityType(type: Int): SupportedActivity = when (type) {
      DetectedActivity.STILL -> STILL
      DetectedActivity.WALKING -> WALKING
      DetectedActivity.RUNNING -> RUNNING
      DetectedActivity.IN_VEHICLE -> IN_VEHICLE
      DetectedActivity.ON_BICYCLE -> ON_BICYCLE
      DetectedActivity.ON_FOOT -> ON_FOOT
      DetectedActivity.UNKNOWN -> UNKNOWN
      DetectedActivity.TILTING -> TILTING
      else -> throw IllegalArgumentException("activity $type not supported")
    }
  }
}