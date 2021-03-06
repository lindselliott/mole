package com.example.lindsay.delta5.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.lindsay.delta5.Application
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

import com.example.lindsay.delta5.R


class MainActivity : AppCompatActivity() {

    enum class Screen {
        DASHBOARD,
        MOLE_INFO,
        HISTORY,
        PROFILE,
        MORE_INFO
    }

    private val fragments: MutableMap<Screen, Fragment> = HashMap()

    private lateinit var deltaApplication: Application
    open var menu: Menu? = null


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
        fragments[Screen.MORE_INFO] = MoreInfoFragment.newInstance()

        switchFragment(Screen.DASHBOARD, false)
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

        fragmentTransaction.replace(R.id.fragment_container, fragment)

        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }

        fragmentTransaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        this.menu = menu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_profile -> {
            switchFragment(Screen.PROFILE, true)
            true
        }
        R.id.save_mole -> {
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as MoleInfoFragment).saveMole()
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as MoleInfoFragment).toggleEditMode()
            true
        }
        R.id.edit_mole -> {
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as MoleInfoFragment).toggleEditMode()
            true
        }
        android.R.id.home -> {

            if((supportFragmentManager.findFragmentById(R.id.fragment_container)) is ProfileFragment) {
                (supportFragmentManager.findFragmentById(R.id.fragment_container) as ProfileFragment).saveUser()
            } else if((supportFragmentManager.findFragmentById(R.id.fragment_container)) is MoleInfoFragment) {
                (supportFragmentManager.findFragmentById(R.id.fragment_container) as MoleInfoFragment).close()
            }

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
