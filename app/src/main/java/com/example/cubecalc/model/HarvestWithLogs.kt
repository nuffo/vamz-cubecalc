package com.example.cubecalc.model

import androidx.room.Embedded
import androidx.room.Relation

data class HarvestWithLogs(
    @Embedded val harvest: Harvest,
    @Relation(
        parentColumn = "logId",
        entityColumn = "logHarvestId"
    )
    val logs: List<Log>
) {}