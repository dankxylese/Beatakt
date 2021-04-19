package com.darkxylese.beatakt

import com.badlogic.gdx.Game
import com.darkxylese.beatakt.screen.GameScreen

class Beatakt : Game() {
    override fun create() {
        setScreen(GameScreen())
    }
}