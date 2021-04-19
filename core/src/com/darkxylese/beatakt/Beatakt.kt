package com.darkxylese.beatakt

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.darkxylese.beatakt.screen.GameScreen
import com.darkxylese.beatakt.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger


const val UNIT_SCALE = 1/10f
private val log = logger<Beatakt>()

class Beatakt : KtxGame<KtxScreen>() {

    val batch: Batch by lazy { SpriteBatch() }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create game instance" }
        addScreen(GameScreen(this))
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        log.debug{"Sprites in batch: ${(batch as SpriteBatch).maxSpritesInBatch}"}
        batch.dispose()
    }
}