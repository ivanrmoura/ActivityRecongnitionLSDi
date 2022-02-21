package br.com.lsdi.activityrecongnition.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ListSleepSegment(@ColumnInfo(name = "start_time") var startTime: String,
                       @ColumnInfo(name = "end_time") var endTime: String,
                       @ColumnInfo(name = "status") var status: String,
                       @ColumnInfo(name = "duration") var duration:Long,
                       @ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Long = 0) {
}