package com.burachevsky.rssfeedreader.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.burachevsky.rssfeedreader.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.setGraph(
            R.navigation.main_nav_graph
        )
    }

    override fun onStart() {
        Log.d("news:MainActivity", "onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.d("news:MainActivity", "onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.d("news:MainActivity", "onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.d("news:MainActivity", "onStop()")
        super.onStop()
    }

    override fun onRestart() {
        Log.d("news:MainActivity", "onRestart()")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d("news:MainActivity", "onDestroy()")
        super.onDestroy()
    }
}
