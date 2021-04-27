package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.audio.AudioService
import com.darkxylese.beatakt.event.GameEventManager
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.log.logger


private val log = logger<BeataktScreen>()
abstract class BeataktScreen(
        val game: Beatakt,
        val gameViewport: Viewport = game.gameViewport,
        val backgroundViewport: Viewport = game.backgroundViewport,
        val uiViewport: Viewport = game.uiViewport,
        val assets: AssetStorage = game.assets,
        val audioService: AudioService = game.audioService,
        val gameEventManager: GameEventManager = game.gameEventManager,
        val preferences: Preferences = game.preferences,
        val stage: Stage = game.stage
) : KtxScreen {


    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        backgroundViewport.update(width, height, true)
        uiViewport.update(width, height, true)
        //log.debug { "RESIZED: " width.toString() + ", " + height.toString() }
    }

}