package com.darkxylese.beatakt.input

import com.darkxylese.beatakt.event.Key

interface InputListener {
    fun keyPressed(key: Key) {}

    fun keyReleased(key: Key) {}
}