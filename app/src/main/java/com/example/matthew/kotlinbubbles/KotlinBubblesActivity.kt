package com.example.matthew.kotlinbubbles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.graphics.Point


class KotlinBubblesActivity : AppCompatActivity() {

    private var kotlinBubblesView: KotlinBubblesView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get display object
        val display = windowManager.defaultDisplay
        // Load the resolution into a Point object
        val size = Point()
        display.getSize(size)

        // Initialize gameView and set it as the view
        kotlinBubblesView = KotlinBubblesView(this, size)
        setContentView(kotlinBubblesView)
    }

    // This method executes when the play starts the game
    override fun onResume() {
        super.onResume()

        // Tell he gameView resume method to execute
        kotlinBubblesView?.resume()
    }

    // This method executes when the player quits the game
    override fun onPause() {
        super.onPause()

        // Tell the gameView pause method to execute
        kotlinBubblesView?.pause()

    }


}
