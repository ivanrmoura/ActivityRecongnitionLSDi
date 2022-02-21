package br.com.lsdi.activityrecongnition.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity()
class ListDetectedActivity(@ColumnInfo(name = "date_time") var dateTime: String,
                           @ColumnInfo(name = "activity_type") var type: String,
                           @ColumnInfo(name = "confidence") var confindence:Int,
                           @ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Long = 0) {
}