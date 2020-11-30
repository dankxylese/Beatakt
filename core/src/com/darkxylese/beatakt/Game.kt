package com.darkxylese.beatakt

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.darkxylese.beatakt.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }
    // use LibGDX's default Arial font
    val font by lazy { BitmapFont() }
    val assets = AssetManager()


    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug {"[Game] Create game instance"}
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
        super.create()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        assets.dispose()
        super.dispose()
    }
}