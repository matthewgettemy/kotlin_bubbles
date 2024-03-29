package com.example.matthew.kotlinbubbles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import java.util.*
import android.graphics.BitmapFactory
import android.util.Log

class Bubble(context: Context, screenX: Int, screenY: Int) {

    companion object {

        // Single bitmap to use for all the bubbles
        // var bitmap1: Bitmap? = null

        val maxSize = 650
        val minSize = 250

        // number of active bubbles
        var numBubbles: Int = 0

    }

    // var width = screenX / 35f
    // var height = screenY / 35f
    var screenX = screenX
    var screenY = screenY
    var width = (minSize..maxSize).random().toFloat()
    var height = width
    val random_x = (0..screenX-width.toInt()).random()
    val random_y = (height.toInt()..screenY-height.toInt()).random()

    val position = RectF(
        random_x / 1f,
        random_y - height,
        random_x + width,
        random_y.toFloat())

    // Pixels per second
    private var speed = 40f

    var bitmap1: Bitmap? = null

    var isVisible = true



    init {
        // Initialize the bitmaps
        bitmap1 = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.bubble)

        bitmap1 = Bitmap.createScaledBitmap(
            bitmap1!!,
            (width.toInt()),
            (height.toInt()),
            false)
    }

    /*
    fun update(fps: Long) {

        val ranX: Float = (0..screenX).random().toFloat()
        val ranY: Float = (0..screenY).random().toFloat()
        // Log.d("myTag", "X: ${ranX}, Y: ${ranY}.")
        /*
        position.left = 100f
        position.top = 100f
        position.right = 100f + width
        position.bottom = 100f + height
        */
    }
    */

}