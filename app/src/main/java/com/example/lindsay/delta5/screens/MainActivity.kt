package com.example.lindsay.delta5.screens

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.lindsay.delta5.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(appBar) // appBar is the id of the toolbar in the layout file

        Log.d("Delta", "In main")
    }
}
