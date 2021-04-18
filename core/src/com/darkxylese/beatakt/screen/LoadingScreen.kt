package com.darkxylese.beatakt.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.assets.AssetManager
import com.darkxylese.beatakt.assets.MusicAssets
import com.darkxylese.beatakt.assets.SoundAssets
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.load
import com.darkxylese.beatakt.ecs.system.MoveSystem
import com.darkxylese.beatakt.ecs.system.PlayerInputSystem
import com.darkxylese.beatakt.ecs.system.SpawnSystem
import com.darkxylese.beatakt.event.GameEventManager
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.graphics.use



class LoadingScreen(private val game: KtxGame<KtxScreen>,
                    private val batch: Batch,
                    private val font: BitmapFont,
                    private val assets: AssetManager,
                    private val camera: OrthographicCamera,
                    private val gameEventManager: GameEventManager,
                    private val ecsEngine: PooledEngine) : KtxScreen {

    override fun show() {
        MusicAssets.values().forEach { assets.load(it) } //TODO: [ALL] make Async
        SoundAssets.values().forEach { assets.load(it) }
        TextureAtlasAssets.values().forEach { assets.load(it) }

        KtxAsync.launch {
            initiateResources()
        }
    }

    private fun initiateResources(){

        ecsEngine.run{
            addSystem(MoveSystem())
            addSystem(PlayerInputSystem(gameEventManager, ecsEngine))
        }

        game.addScreen(
                GameScreen(
                        batch,
                        font,
                        assets,
                        camera,
                        ecsEngine,
                        gameEventManager
                )
        )
    }

    override fun render(delta: Float) {
        font.data.setScale(2.5f)

        // continue loading our assets
        assets.update()

        camera.update()
        batch.projectionMatrix = camera.combined

        batch.use {
            font.draw(it, "Welcome to Beatakt ", 100f, 150f)

            //TODO implement loading screen
            if (assets.isFinished) {
                font.draw(it, "Tap anywhere to begin!", 100f, 100f)
            } else {
                font.draw(it, "Loading assets...", 100f, 100f)
            }
        }


        if (Gdx.input.isTouched && assets.isFinished) {
            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<GameScreen>()
        }
    }
}
