package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.darkxylese.beatakt.Game
import com.darkxylese.beatakt.assets.MusicAssets
import com.darkxylese.beatakt.assets.SoundAssets
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.load
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreen(val game: Game) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 720f, 1280f) }

    override fun render(delta: Float) {
        // continue loading our assets
        game.assets.update()

        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.use {
            game.font.draw(it, "Welcome to Beatakt ", 100f, 150f)

            //TODO implement loading screen
            if (game.assets.isFinished) {
                game.font.draw(it, "Tap anywhere to begin!", 100f, 100f)
            } else {
                game.font.draw(it, "Loading assets...", 100f, 100f)
            }
        }


        if (Gdx.input.isTouched && game.assets.isFinished) {
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }
    }

    override fun show() {
        MusicAssets.values().forEach { game.assets.load(it) }
        SoundAssets.values().forEach { game.assets.load(it) }
        TextureAtlasAssets.values().forEach { game.assets.load(it) }
    }
}
