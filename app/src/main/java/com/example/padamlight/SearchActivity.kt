package com.example.padamlight

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
//import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.padamlight.utils.Toolbox
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import java.util.*

class SearchActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //@BindView(R.id.spinner_from)
    //lateinit var mSpinnerFrom: Spinner
    //@BindView(R.id.spinner_to)
    //lateinit var mSpinnerTo: Spinner
    //@BindView(R.id.button_search)
    //var mButtonSearch: Button? = null
    //@BindView(R.id.drawerlayout)
    //lateinit var drawerLayout: DrawerLayout
    //@BindView(R.id.nav)
    //lateinit var navView: NavigationView

    lateinit var mSpinnerFrom: Spinner
    lateinit var mSpinnerTo: Spinner
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var mButtonSearch: Button
    lateinit var toggle: ActionBarDrawerToggle
    private var mapDelegate: MapActionsDelegate? = null
    private lateinit var mFromList: HashMap<String, Suggestion>

    companion object {
        /*
           Constants FROM lat lng
        */
        val PADAM = LatLng(48.8609, 2.349299999999971)
        val TAO = LatLng(47.9022, 1.9040499999999838)
        val FLEXIGO = LatLng(48.8598, 2.0212400000000343)
        val LA_NAVETTE = LatLng(48.8783804, 2.590549)
        val ILEVIA = LatLng(50.632, 3.05749000000003)
        val NIGHT_BUS = LatLng(45.4077, 11.873399999999947)
        val FREE2MOVE = LatLng(33.5951, -7.618780000000015)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Binding UI elements defined below
        ButterKnife.bind(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Padam Light"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerlayout)as DrawerLayout
        navView = findViewById(R.id.nav) as NavigationView

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));
        drawerLayout.addDrawerListener(toggle)
        navView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        mButtonSearch = findViewById(R.id.button_search)
        this.mButtonSearch.setOnClickListener{
            onClickSearch()
        }

        mSpinnerFrom = findViewById(R.id.spinner_from) as Spinner
        mSpinnerTo = findViewById(R.id.spinner_to) as Spinner

        initializeTextViews()
        initSpinners()
        initMap()

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //@OnClick(R.id.button_search)
    private fun onClickSearch() {
        /*
            Retrieve selection of "From" spinner
         */
        val selectedFrom = mSpinnerFrom.selectedItem.toString()
        if (!selectedFrom.isEmpty()) {
            mapDelegate?.clearMap()
            val selectedFromSuggestion = mFromList[selectedFrom]
            if(selectedFromSuggestion != null) {
                mapDelegate?.updateMarker(
                    MarkerType.PICKUP,
                    selectedFrom,
                    selectedFromSuggestion.getLatLng()
                )
                mapDelegate?.updateMap(selectedFromSuggestion.getLatLng())
            }
        }

        val selectedTo = mSpinnerTo.selectedItem.toString()
        if (!selectedTo.isEmpty()) {
            val selectedToSuggestion = mFromList[selectedTo]
            if (selectedToSuggestion != null) {
                mapDelegate?.updateMarker(
                    MarkerType.DROPOFF,
                    selectedTo,
                    selectedToSuggestion.getLatLng()
                )

                mapDelegate?.updateMap(selectedToSuggestion.getLatLng())
            }
        }
    }

    private fun initializeTextViews() {
        mButtonSearch.text = resources.getString(R.string.button_search)
    }

    private fun initMap() {
        /*
            Instanciate MapFragment to get the map on the page
         */
        var mapFragment = MapsFragment.newInstance()

        supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_map, mapFragment)
            .commitAllowingStateLoss()

        this.mapDelegate = mapFragment

    }

    /**
     * Initialize spinners from and to
     */
    private fun initSpinners() {
        var fromList: List<String> = Toolbox.formatHashmapToList(initFromHashmap())
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fromList)
        mSpinnerFrom.adapter = adapter
        val to_adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fromList)
        mSpinnerTo.adapter = to_adapter
    }

    /**
     * Using Hashmap to initialize FROM suggestion list
     */
    private fun initFromHashmap(): HashMap<String, Suggestion> {
        mFromList = HashMap<String, Suggestion>()
        mFromList["Padam"] = Suggestion(SearchActivity.PADAM)
        mFromList["Tao Résa'Est"] = Suggestion(SearchActivity.TAO)
        mFromList["Flexigo"] = Suggestion(SearchActivity.FLEXIGO)
        mFromList["La Navette"] = Suggestion(SearchActivity.LA_NAVETTE)
        mFromList["Ilévia"] = Suggestion(SearchActivity.ILEVIA)
        mFromList["Night Bus"] = Suggestion(SearchActivity.NIGHT_BUS)
        mFromList["Free2Move"] = Suggestion(SearchActivity.FREE2MOVE)
        return mFromList
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_map -> {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(Gravity.LEFT)
                }
            }
            R.id.resume -> {
                val intent = Intent(this@SearchActivity, PropositionsActivity::class.java)
                startActivity(intent)
            }
        }
        return true

    }

}