package com.example.matthew.kotlinbubbles

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.view.SurfaceView
import android.util.Log
import android.view.MotionEvent


class KotlinBubblesView(context: Context,
                        private val size: Point)
    : SurfaceView(context),
    Runnable {



    private val gameThread = Thread(this)

    // A boolean which we will set and unset
    private var playing = false

    // Game is paused at the start
    private var paused = true

    // A Canvas and a Paint object
    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    // The bubble
    private var bubble: Bubble = Bubble(context, size.x, size.y)

    // How menacing should the sound be?
    private var menaceInterval: Long = 1000

    // When did we last play a menacing sound
    private var lastMenaceTime = System.currentTimeMillis()

    // Number of bubbles that have been popped
    private var numPops: Int = 0

    // For making a noise
    private val soundPlayer = SoundPlayer(context)

    // To remember the high score
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "Kotlin Bubbles",
        Context.MODE_PRIVATE)


    private var highScore = prefs.getInt("highScore", 0)

    override fun run() {
        // This variable tracks the game frame rate
        var fps: Long = 0

        while (playing) {

            // Capture the current time
            val startFrameTime = System.currentTimeMillis()

            // Update the frame
            if (!paused) {
                update(fps)
            }

            // Draw the frame
            draw()

            //Calculate the fps rate this frame
            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }

            // Play a sound based on menace level
            if (!paused && ((startFrameTime - lastMenaceTime) > menaceInterval)) {
                //menacePlayer()
            }


        }
    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    fun resume() {
        playing = true
        prepareLevel()
        gameThread.start()
    }

    private fun prepareLevel() {
        // set up the level
    }

    fun pause() {
        playing = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }

        val prefs = context.getSharedPreferences(
            "Kotlin Invaders",
            Context.MODE_PRIVATE)

        val oldHighScore = prefs.getInt("highScore", 0)

        if (highScore > oldHighScore) {
            val editor = prefs.edit()

            editor.putInt("highScore", highScore)
            editor.apply()
        }
    }

    private fun draw() {
        // Make sure our drawing surface is valid or the game will crash


        if (holder.surface.isValid) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas()

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Choose the brush color for drawing
            paint.color = Color.argb(255, 0, 255, 0)

            // Draw all the game objects here
            // Now draw the player spaceship
            canvas.drawBitmap(bubble.bitmap, bubble.position.left,
                              bubble.position.top, paint)

            /*
            // Draw the invaders
            for (invader in invaders) {
                if (invader.isVisible) {
                    if (uhOrOh) {
                        canvas.drawBitmap(invader.bitmap1!!,
                            invader.position.left,
                            invader.position.top,
                            paint)
                    } else {
                        canvas.drawBitmap(invader.bitmap2!!,
                            invader.position.left,
                            invader.position.top,
                            paint)
                    }
                }
            }

            // Draw the bricks
            for (brick in bricks) {
                if (brick.isVisible) {
                    canvas.drawRect(brick.position, paint)
                }
            }

            // Draw the players playerBullet if active
            if (playerBullet.isActive) {
                canvas.drawRect(playerBullet.position, paint)
            }

            // Draw the invaders bullets
            for (bullet in invadersBullets) {
                if (bullet.isActive) {
                    canvas.drawRect(bullet.position, paint)
                }
            }
            */
            // Draw the score and remaining lives
            // Change the brush color
            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = 70f
            canvas.drawText("Score: ${numPops}   Lives: 1 Wave: " +
                    "1 HI: $highScore", 20f, 75f, paint)


            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas)

        }

    }

    private fun update(fps: Long) {
        // Update the state of all the game objects

        // Move the bubble
        bubble.update(fps)

        /*
        // Did an invader bump into the side of the screen
        var bumped = false

        // Has the player lost
        var lost = false

        // Update all the invaders if visible
        for (invader in invaders) {

            if (invader.isVisible) {
                // Move the next invader
                invader.update(fps)

                // Does he want to take a shot?
                if (invader.takeAim(playerShip.position.left,
                        playerShip.width,
                        waves)) {

                    // If so, try to spawn a bullet
                    if (invadersBullets[nextBullet].shoot(invader.position.left
                                + invader.width / 2,
                            invader.position.top, playerBullet.down)) {
                        // Shot fired
                        // Prepare for the next shot
                        nextBullet++

                        // Loop back to the first one if we have reached the last
                        if (nextBullet == maxInvaderBullets) {
                            // This stops the firing of bullet
                            // until one completes its journey
                            // Because if bullet 0 is still active
                            // shoot returns false
                            nextBullet = 0
                        }
                    }
                }

                // If that move caused them to bump
                // the screen change bumped to true
                if (invader.position.left > size.x - invader.width
                    || invader.position.left < 0) {

                    bumped = true

                }
            }
        }

        // Update the players playerBullet if active
        if (playerBullet.isActive) {
            playerBullet.update(fps)
        }

        // Update the invaders bullets if active
        for (bullet in invadersBullets) {
            if (bullet.isActive) {
                bullet.update(fps)
            }
        }

        // Did an invader bump into the edge of the screen
        if (bumped) {

            // Move all the invaders down and change direction
            for (invader in invaders) {
                invader.dropDownAndReverse(waves)
                // Have the invaders landed
                if (invader.position.bottom >= size.y && invader.isVisible) {
                    lost = true;
                }
            }
        }

        // Has the players bullet hit
        // the top of the screen
        if (playerBullet.position.bottom < 0) {
            playerBullet.isActive = false
        }


        // Has an invaders bullet
        // hit the bottom of the screen
        for (bullet in invadersBullets) {
            if (bullet.position.top > size.y) {
                bullet.isActive = false
            }
        }

        // Has the players playerBullet hit an invader
        if (playerBullet.isActive) {
            for (invader in invaders) {
                if (invader.isVisible) {
                    if (RectF.intersects(playerBullet.position,
                            invader.position)) {
                        invader.isVisible = false

                        soundPlayer.playSound(
                            SoundPlayer.invaderExplodeID)

                        playerBullet.isActive = false
                        Invader.numberOfInvaders --
                        score += 10
                        if (score > highScore) {
                            highScore = score
                        }

                        // Has the player cleared the level?
                        // if (score == numberInvaders * 10 * waves)
                        if (Invader.numberOfInvaders == 0) {
                            paused = true
                            lives ++
                            invaders.clear()
                            bricks.clear()
                            invadersBullets.clear()
                            prepareLevel()
                            waves ++
                            break
                        }

                        // Don't check any more invaders
                        break
                    }
                }
            }
        }


        // Has an alien bullet hit a shelter brick
        for (bullet in invadersBullets) {
            if (bullet.isActive) {
                for (brick in bricks) {
                    if (brick.isVisible) {
                        if (RectF.intersects(bullet.position, brick.position)) {
                            // A collision has occurred
                            bullet.isActive = false
                            brick.isVisible = false
                            soundPlayer.playSound(SoundPlayer.damageShelterID)
                        }
                    }
                }
            }
        }


        // Has a player bullet hit a shelter brick
        if (playerBullet.isActive) {
            for (brick in bricks) {
                if (brick.isVisible) {
                    if (RectF.intersects(playerBullet.position, brick.position)) {
                        // A collision
                        playerBullet.isActive = false
                        brick.isVisible = false
                        soundPlayer.playSound(SoundPlayer.damageShelterID)
                    }
                }
            }
        }

        // Has an invader bullet hit
        // the player ship?
        for (bullet in invadersBullets) {
            if (bullet.isActive) {
                if (RectF.intersects(playerShip.position, bullet.position)) {
                    bullet.isActive = false
                    lives --
                    soundPlayer.playSound(SoundPlayer.playerExplodeID)

                    // Is it game over?
                    if (lives == 0) {
                        lost = true
                        break
                    }
                }
            }
        }

        if (lost) {
            paused = true
            lives = 3
            waves = 1
            invaders.clear()
            bricks.clear()
            invadersBullets.clear()
            prepareLevel()
        }
        */
    }


    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {


        when (motionEvent.action and MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            // or moved their finger while touching the screen
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE-> {
                paused = false
                    Log.d("myTag", "motion detect at ${motionEvent.x}, ${motionEvent.y}!")
                    if (bubble.position.intersects(motionEvent.x, motionEvent.y,
                        motionEvent.x, motionEvent.y)) {
                        bubble.pop()
                        numPops++
                        if (numPops > highScore) {
                            highScore = numPops
                        }
                    }
                /*
                    if (motionEvent.y > size.y - size.y / 8) {
                        if (motionEvent.x > size.x / 2) {
                            playerShip.moving = PlayerShip.right
                        } else {
                            playerShip.moving = PlayerShip.left
                        }


                    }

                    if (motionEvent.y < size.y - size.y / 8) {
                        // Shots fired
                        if (playerBullet.shoot(
                                playerShip.position.left + playerShip.width / 2f,
                                playerShip.position.top,
                                playerBullet.up)) {

                            soundPlayer.playSound(SoundPlayer.shootID)
                        }
                    }
                    */
                }


            /*
                // Player has removed finger from screen
                MotionEvent.ACTION_POINTER_UP,
                MotionEvent.ACTION_UP -> {
                    if (motionEvent.y > size.y - size.y / 10) {
                        playerShip.moving = PlayerShip.stopped
                    }
                }
                */
            }

        return true
    }

}