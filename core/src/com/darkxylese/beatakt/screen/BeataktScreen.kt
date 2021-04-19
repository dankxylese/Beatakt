package com.darkxylese.beatakt.screen

import com.badlogic.gdx.graphics.g2d.Batch
import com.darkxylese.beatakt.Beatakt
import ktx.app.KtxScreen

abstract class BeataktScreen(
        val game: Beatakt,
        val batch: Batch = game.batch
) : KtxScreen {

}