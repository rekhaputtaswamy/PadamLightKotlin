package com.example.padamlight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment(), MapActionsDelegate, OnMapReadyCallback {

    @Nullable
    private var mMap: GoogleMap? = null

    companion object {
        var mapFragment : SupportMapFragment?=null

        fun newInstance() : MapsFragment {
            return MapsFragment()
        }
    }

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap as GoogleMap
        mMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(47.902964, 1.9092510000000402),
                16f
            )
        )
    }

    override fun updateMap(vararg latLngs: LatLng) {
        if (mMap != null) {
            val builder = LatLngBounds.Builder()
            for (latLng in latLngs) {
                builder.include(latLng)
            }
            val bounds = builder.build()
            animateMapCamera(bounds)
        }
    }

    override fun updateMarker(type: MarkerType, markerName: String, markerLatLng: LatLng) {
        if (mMap != null) {
            val marker = MarkerOptions()
                .position(markerLatLng)
                .title(markerName)
                .icon(getMarkerIcon(type))
            mMap?.addMarker(marker)
        }
    }



    override fun clearMap() {
        if (mMap != null) {
            mMap?.clear()
        }
    }

    private fun animateMapCamera(bounds: LatLngBounds) {
        if (mMap != null) {
            mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    private fun getMarkerIcon(type: MarkerType): BitmapDescriptor {
        @DrawableRes var icon: Int
        icon = when (type) {
            MarkerType.PICKUP -> {
                R.drawable.ic_pickup
            }
            MarkerType.DROPOFF -> {
                R.drawable.ic_dropoff
            }
        }
        return BitmapDescriptorFactory.fromResource(icon)
    }

}

/**
 * Map interface
 * Implement this in your page where there is a map to use map methods
 */
interface MapActionsDelegate {
    fun updateMap(vararg latLngs: LatLng)
    fun updateMarker(
        type: MarkerType,
        markerName: String,
        markerLatLng: LatLng
    )
    fun clearMap()
}

/**
 * Market enum
 * Define if marker is pickup type or dropoff type
 */
enum class MarkerType {
    PICKUP, DROPOFF
}
