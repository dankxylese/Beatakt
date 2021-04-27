package com.darkxylese.beatakt

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.darkxylese.beatakt.assets.MusicAsset
import com.darkxylese.beatakt.assets.TextureAsset
import com.darkxylese.beatakt.assets.TextureAtlasAsset
import com.darkxylese.beatakt.audio.AudioService
import com.darkxylese.beatakt.audio.DefaultAudioService
import com.darkxylese.beatakt.ecs.system.*
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.set


const val UNIT_SCALE = 1/10f
const val V_WIDTH = 9
const val V_HEIGHT = 16
const val V_WIDTH_PIXELS = 720
const val V_HEIGHT_PIXELS = 1280

private val log = logger<Beatakt>()

class Beatakt : KtxGame<KtxScreen>() {
    val uiViewport = FitViewport(V_WIDTH_PIXELS.toFloat(), V_HEIGHT_PIXELS.toFloat())
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val assets : AssetStorage by lazy {
        //init asset storage. Call ktxasync beforehand to start async api
        KtxAsync.initiate()
        AssetStorage()
    }
    val audioService : AudioService by lazy { DefaultAudioService(assets) }
    val preferences : Preferences by lazy { Gdx.app.getPreferences("beatakt") }

    val batch: Batch by lazy { SpriteBatch() }
    val gameEventManager = GameEventManager()
    val engine: Engine by lazy { //by first use the assets are already loaded

        preferences["key"] = 3.5f

        PooledEngine().apply {

            val graphicsAtlas = assets[TextureAtlasAsset.GAME_GRAPHICS.descriptor]
            addSystem(PlayerInputSystem(gameViewport, gameEventManager))
            addSystem(MoveSystem())
            addSystem(TextureSystem(
                    graphicsAtlas.findRegion("hitboxUnifiedCentered270a"),
                    graphicsAtlas.findRegion("hitRed270a"),
                    graphicsAtlas.findRegion("hit270a")
            ))
            addSystem(RenderSystem(
                    batch,
                    gameViewport,
                    uiViewport,
                    assets[TextureAsset.BACKGROUND1.descriptor],
                    assets[TextureAsset.BACKGROUND2.descriptor]))
            addSystem(RemoveSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create game instance" }
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        log.debug{"Sprites in batch: ${(batch as SpriteBatch).maxSpritesInBatch}"}
        batch.dispose()
        assets.dispose()
    }
}