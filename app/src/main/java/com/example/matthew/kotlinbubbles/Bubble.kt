package com.example.matthew.kotlinbubbles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import java.util.*
import android.graphics.BitmapFactory
import android.util.Log

class Bubble(context: Context, screenX: Int, screenY: Int) {

    // var width = screenX / 35f
    // var height = screenY / 35f
    var screenX = screenX
    var screenY = screenY
    var width = 400f
    var height = 400f
    val random_x = (0..screenX).random()
    val random_y = (0..screenY).random()

    val position = RectF(
        random_x / 1f,
        random_y-height,
        random_x + width,
        random_y.toFloat())

    // Pixels per second
    private var speed = 40f

    var isVisible = true


    // Initialize the bitmaps
    var bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.bubble)

    init {

        bitmap = Bitmap.createScaledBitmap(
            bitmap!!,
            (width.toInt()),
            (height.toInt()),
            false)
    }

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
    fun pop() {
        val ranX: Float = (0..(screenX - width.toInt())).random().toFloat()
        val ranY: Float = (0.. (screenY - height.toInt())).random().toFloat()
        Log.d("myTag", "X: ${ranX}, Y: ${ranY}.")

        position.left = ranX
        position.top = ranY
        position.right = ranX + width
        position.bottom = ranY + height

    }

}