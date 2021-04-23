package com.darkxylese.beatakt.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.darkxylese.beatakt.Beatakt
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger


private val log = logger<GameScreen>()
abstract class BeataktScreen(
        val game: Beatakt,
        val batch: Batch = game.batch,
        val gameViewport: Viewport = game.gameViewport,
        val engine: Engine = game.engine
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        //log.debug { "RESIZED: " width.toString() + ", " + height.toString() }
    }

}