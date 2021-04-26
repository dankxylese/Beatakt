package com.darkxylese.beatakt

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import com.darkxylese.beatakt.ecs.system.*
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.screen.GameScreen
import com.darkxylese.beatakt.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger


const val UNIT_SCALE = 1/10f
const val V_WIDTH = 9
const val V_HEIGHT = 16
const val V_WIDTH_PIXELS = 720
const val V_HEIGHT_PIXELS = 1280

private val log = logger<Beatakt>()

class Beatakt : KtxGame<KtxScreen>() {
    val uiViewport = FitViewport(V_WIDTH_PIXELS.toFloat(), V_HEIGHT_PIXELS.toFloat())
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())

    val graphicsAtlas by lazy{ TextureAtlas(Gdx.files.internal("images/GameElements.atlas")) }
    val backgroundTexture1 by lazy { Texture(Gdx.files.internal("images/background1.png")) }
    val backgroundTexture2 by lazy { Texture(Gdx.files.internal("images/background2.png")) }

    val batch: Batch by lazy { SpriteBatch() }
    val gameEventManager = GameEventManager()
    val engine: Engine by lazy {
        PooledEngine().apply {
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
                    backgroundTexture1,
                    backgroundTexture2))
            addSystem(RemoveSystem())
        }
    }

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
        graphicsAtlas.dispose()
    }
}