package com.example.lindsay.delta5

import android.util.Log

class Application: android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Realm and load all relevant data

        initUser()
    }

    /**
     * Will load the user information if it exists and will create a new user if there is none in yet
     */
    private fun initUser() {
        Log.d("Delta", "Initializing user from Application")
    }
}