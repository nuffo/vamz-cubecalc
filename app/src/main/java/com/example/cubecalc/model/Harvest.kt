package com.example.cubecalc.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Entity(tableName = "harvests")
data class Harvest(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String?,
    val date: Date,
    val spruceLogsCount: Int,
    val beechLogsCount: Int,
    val firLogsCount: Int,
    val spruceCubeMetres: Double,
    val beechCubeMetres: Double,
    val firCubeMetres: Double,
    val createdAt: Long
): Parcelable {
    fun dateToString(format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(date)
    }
}