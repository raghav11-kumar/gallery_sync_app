package com.example.gallery_sync_app.screens.ble.data

data class CombinedMeteringData(
    val voltage: List<Double>,
    val current: List<Double>,
    val activePower: List<Double>,
    val apparentPower: List<Double>,
    val reactivePower: List<Double>,
    val frequency: List<Double>,

    val pf: List<Double>,
    val st: List<Double>,
    val qt: List<Double>
)

