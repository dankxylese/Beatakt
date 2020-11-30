package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import com.darkxylese.beatakt.Game
import com.darkxylese.beatakt.assets.MusicAssets
import com.darkxylese.beatakt.assets.SoundAssets
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.get
import ktx.assets.pool
import ktx.app.KtxScreen
import ktx.assets.invoke
import ktx.collections.iterate
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(val game: Game) : KtxScreen {

    // load the image for bit blocks, 128x128 pixels
    private val hitImage = game.assets[TextureAtlasAssets.Game].findRegion("hit")
    // load the hit sound effect and the audio background music TODO: change this to track song position / manage it elsewhere
    private val hitSound = game.assets[SoundAssets.Hit]
    private val testAudio = game.assets[MusicAssets.Song]
    // The camera ensures we can render using our target resolution of 720x1280
    //    pixels no matter what the screen resolution is.
    private val camera = OrthographicCamera().apply { setToOrtho(false, 720f, 1280f) }
    // create Rectangle will logically represent our touch area
    // center the touch horizontally, locked 20px on the bottom screen
    // all for now. Later track touch coordinates and do logic
    private val oneTouch = Rectangle(720f / 2f - 64f / 2f, 20f, 64f, 64f)
    // create the touchPos to store mouse click position for desktop
    private val touchPos = Vector3()
    // create the hitBlocks array and spawn the first hitBlock
    private val hitBlockPool = pool { Rectangle() } // pool to reuse raindrop rectangles
    private val activeHitBlocks = Array<Rectangle>() // gdx, not Kotlin Array
    private val dropRate = 6
    private var lastDropTime = 0L
    private var accurateHits = 0

    private fun spawnRaindrop() {
        activeHitBlocks.add(hitBlockPool().set(MathUtils.random(0f, 720f - 64f), 1280f, 64f, 64f))
        lastDropTime = TimeUtils.nanoTime()
    }

    private fun spawnHit() {

    }

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.batch.projectionMatrix = camera.combined

        // begin a new batch and draw the bucket and all drops
        game.batch.use { batch ->
            game.font.draw(batch, "Hits: $accurateHits", 0f, 1280f)
            activeHitBlocks.forEach { hitBlock -> batch.draw(hitImage, hitBlock.x, hitBlock.y) }
        }

        // process user input
        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            oneTouch.x = touchPos.x - 64f / 2f
            hitSound.play()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            oneTouch.x -= 200 * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            oneTouch.x += 200 * delta
        }

        // make sure the bucket stays within the screen bounds
        oneTouch.x = MathUtils.clamp(oneTouch.x, 0f, 720f - 64f)

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 500_000_000L) {
            spawnRaindrop()
        }

        // move the raindrops, remove any that are beneath the bottom edge of the
        //    screen or that hit the bucket.  In the latter case, play back a sound
        //    effect also
        activeHitBlocks.iterate { hitBlock, iterator ->
            hitBlock.y -= dropRate * 200 * delta
            if (hitBlock.y + 64 < 0)
                iterator.remove()
            hitBlockPool(hitBlock)
                log.debug { "[GameScreen] Missed a hitBlock! Pool free objects: ${hitBlockPool.free}" }



            if (hitBlock.overlaps(oneTouch)) {
                accurateHits++
                iterator.remove()
                hitBlockPool(hitBlock)
                log.debug { "[GameScreen] Pool free objects: ${hitBlockPool.free}" }
            }
        }
    }

    override fun show() {
        // start the playback of the background music when the screen is shown
        testAudio.play()
        spawnRaindrop()
    }

    override fun dispose() {
        log.debug { "[GameScreen] Disposing ${this.javaClass.simpleName}" }
        testAudio.dispose()
    }
}