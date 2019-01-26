package com.example.lindsay.delta5.screens

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.example.lindsay.delta5.R

import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d("Delta", "In splash")

        switchToMainApp()
    }

    private fun switchToMainApp() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun switchToLoginRegister() {
        startActivity(Intent(this, LoginRegisterActivity::class.java))
    }

}
