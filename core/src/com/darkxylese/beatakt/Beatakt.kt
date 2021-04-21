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
import com.darkxylese.beatakt.screen.GameScreen
import com.darkxylese.beatakt.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger


const val UNIT_SCALE = 1/10f
const val V_WIDTH = 9
const val V_HEIGHT = 16

private val log = logger<Beatakt>()

class Beatakt : KtxGame<KtxScreen>() {
    val gameViewport = FitViewport(9f, 16f)

    val graphicsAtlas by lazy{ TextureAtlas(Gdx.files.internal("images/GameElements.atlas")) }
    val batch: Batch by lazy { SpriteBatch() }
    val engine: Engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(TextureSystem(
                    graphicsAtlas.findRegion("hitboxUnifiedCentered270a"),
                    graphicsAtlas.findRegion("hitRed270a"),
                    graphicsAtlas.findRegion("hit270a")
            ))
            addSystem(RenderSystem(batch, gameViewport))
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