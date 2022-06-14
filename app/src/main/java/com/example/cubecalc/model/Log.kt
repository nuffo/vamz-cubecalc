package com.example.cubecalc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.math.pow

@Entity(tableName = "logs", foreignKeys = [
    ForeignKey(
        entity = Harvest::class,
        parentColumns = ["id"],
        childColumns = ["logHarvestId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Log(
    @PrimaryKey(autoGenerate = true)
    val logId: Int,
    val logHarvestId: Int,
    val type: String?,
    val length: Int,
    val diameter: Int,
    val cubeMetres: Double
) {}