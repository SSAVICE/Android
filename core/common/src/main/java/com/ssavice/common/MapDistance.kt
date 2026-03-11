package com.ssavice.common

fun mapDistanceKm(distance: Double): String {
    return if(distance < 1) {
        "${(distance * 1000).toInt()}m"
    }
    else {
        "%.1fkm".format(distance)
    }
}
