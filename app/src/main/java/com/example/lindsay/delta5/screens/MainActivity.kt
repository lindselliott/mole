package com.example.lindsay.delta5.screens

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.MenuItem
import com.example.lindsay.delta5.Application
import com.example.lindsay.delta5.entities.Mole
import com.example.lindsay.delta5.models.MoleModel
import com.example.lindsay.delta5.utils.DateUtils
import com.example.lindsay.delta5.utils.ImageUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

import com.example.lindsay.delta5.R

class MainActivity : AppCompatActivity() {

    enum class Screen {
        DASHBOARD,
        MOLE_INFO,
        HISTORY,
        PROFILE
    }

    private val fragments: MutableMap<Screen, Fragment> = HashMap()

    private lateinit var deltaApplication: Application

    private  var frag: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(appBar) // appBar is the id of the toolbar in the layout file

        deltaApplication = application as Application

        initFragments()
    }

    private fun initFragments() {
        fragments[Screen.DASHBOARD] = DashboardFragment.newInstance()
        fragments[Screen.MOLE_INFO] = MoleInfoFragment.newInstance()
        fragments[Screen.HISTORY] = MoleInfoFragment.newInstance() // FIXME: Make it the proper fragment
        fragments[Screen.PROFILE] = ProfileFragment.newInstance()

        switchFragment(Screen.DASHBOARD)
    }

    open fun switchFragment(screen: Screen, addToBackStack: Boolean = true, vararg params: String) {

        Log.d("Delta", "Transaction begin")
        val fragment = fragments[screen] ?: return
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // Set params if present
        if (params.isNotEmpty()) {
            val bundle = Bundle()
            for (i in params.indices) {
                bundle.putString("PARAM" + (i + 1), params[i])
            }
            fragment.arguments = bundle
        }
        
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_profile -> {
            switchFragment(Screen.PROFILE, true)
            true
        }
        android.R.id.home -> {
            supportFragmentManager.popBackStack()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


}
