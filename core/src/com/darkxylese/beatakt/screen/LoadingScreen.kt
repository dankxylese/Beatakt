package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.darkxylese.beatakt.Beatakt
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class LoadingScreen(game: Beatakt) : BeataktScreen(game) {
    override fun show() {
        log.debug { "Loading Screen is Shown" }
    }

    override fun render(delta: Float) {
        if(Gdx.input.justTouched()){
            game.setScreen<GameScreen>()
        }
    }
}