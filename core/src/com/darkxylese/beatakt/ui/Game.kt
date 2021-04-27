package com.darkxylese.beatakt.ui

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.screen.GameScreen
import com.darkxylese.beatakt.screen.LoadingScreen
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.async.KtxAsync
import ktx.inject.register
import ktx.log.debug
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>() {
    private val context = Context()
    val gameEventManager by lazy { GameEventManager() }
    val ecsEngine by lazy { PooledEngine() }
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG


        KtxAsync.initiate()
        KtxAsync.launch{
            context.register {
                bindSingleton(this@Game)
                bindSingleton<Batch>(SpriteBatch())
                bindSingleton(BitmapFont())
                bindSingleton(AssetManager())
                //bindSingleton(OrthographicCamera().apply { setToOrtho(false, 720f, 1280f) })
                //bindSingleton(OrthographicCamera().apply { setToOrtho(false, 512f, 910f) })
                bindSingleton(OrthographicCamera().apply { setToOrtho(false, 1080f, 1920f) })
                bindSingleton(PooledEngine())
            }

            Gdx.input.inputProcessor = InputMultiplexer(gameEventManager)

            addScreen(
                LoadingScreen(
                        this@Game, // game instance to switch screens
                        context.inject(), // batch
                        context.inject(), // BitmapFont
                        context.inject(), // AssetManager
                        context.inject(), // Camera
                        gameEventManager,  // EventManager
                        ecsEngine // entity component engine
                )
            )
            setScreen<LoadingScreen>()

        }

        //super.create()
    }

    override fun dispose() {
        log.debug { "Entities in engine at kill time: ${context.inject<PooledEngine>().entities.size()}" }
        context.dispose()
        super.dispose()
    }
}