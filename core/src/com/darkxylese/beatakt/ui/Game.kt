package com.darkxylese.beatakt.ui

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.darkxylese.beatakt.screen.GameScreen
import com.darkxylese.beatakt.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.log.debug
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>() {
    private val context = Context()

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG


        context.register {
            bindSingleton(this@Game)
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton(BitmapFont())
            bindSingleton(AssetManager())
            //bindSingleton(OrthographicCamera().apply { setToOrtho(false, 720f, 1280f) })
            //bindSingleton(OrthographicCamera().apply { setToOrtho(false, 512f, 910f) })
            bindSingleton(OrthographicCamera().apply { setToOrtho(false, 1080f, 1920f) })
            bindSingleton(PooledEngine())

            addScreen(LoadingScreen(inject(), inject(), inject(), inject(), inject()))
            addScreen(GameScreen(inject(), inject(), inject(), inject(), inject()))

        }
        setScreen<LoadingScreen>()
        super.create()
    }

    override fun dispose() {
        log.debug { "Entities in engine at kill time: ${context.inject<PooledEngine>().entities.size()}" }
        context.dispose()
        super.dispose()
    }
}