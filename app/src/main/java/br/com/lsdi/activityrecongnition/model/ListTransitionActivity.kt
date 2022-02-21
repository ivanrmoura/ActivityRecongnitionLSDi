package br.com.lsdi.activityrecongnition.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
class ListTransitionActivity(@ColumnInfo(name = "datetime") var datetime: String,
                             @ColumnInfo(name = "activity_type") var activityType: String,
                             @ColumnInfo(name = "transition_type") var transitionType: String,
                             @ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Long = 0) {
}