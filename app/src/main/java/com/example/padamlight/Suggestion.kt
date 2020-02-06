package com.example.padamlight

import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng

class Suggestion(latLng: LatLng) {
    private var latLng: LatLng

    @DrawableRes
    private var icon = 0

    init {
        this.latLng = latLng
        icon = R.drawable.ic_favorite
    }

    fun getLatLng(): LatLng {
        return latLng
    }

    @DrawableRes
    fun getIcon(): Int {
        return icon
    }

}