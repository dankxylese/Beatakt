package com.darkxylese.beatakt.event

import com.badlogic.gdx.utils.Array
import com.darkxylese.beatakt.input.InputListener
import com.darkxylese.beatakt.screen.GameScreen
import ktx.app.KtxInputAdapter
import ktx.log.logger

private val log = logger<GameScreen>()

enum class Key {
    NONE, ONE, TWO, THREE, FOUR
}
class GameEventManager : KtxInputAdapter {
    // input event related stuff
    private val inputListeners = Array<InputListener>()
    private var ignoreInput = false

    fun addInputListener(listener: InputListener) = inputListeners.add(listener)

    fun removeInputListener(listener: InputListener) = inputListeners.removeValue(listener, true)

    fun dispatchInputKeyPressEvent(key: Key) {
        //if (ignoreInput) return
        inputListeners.forEach { it.keyPressed(key) }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        //if (ignoreInput) return true

        log.debug{"X: $screenX, Y: $screenY"}

        //TODO: For now, Y is limited (>250) so that future UI (Pause Button) is accessible at the very top (<250)
        if ((screenX in 0..270) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.ONE)
        }
        if ((screenX in 270..540) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.TWO)
        }
        if ((screenX in 540..810) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.THREE)
        }
        if ((screenX in 810..1080) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.FOUR)
        }

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if ((screenX in 0..270) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.NONE)
        }
        if ((screenX in 270..540) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.NONE)
        }
        if ((screenX in 540..810) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.NONE)
        }
        if ((screenX in 810..1080) && (screenY in 250..1920)){
            dispatchInputKeyPressEvent(Key.NONE)
        }
        return true
    }


}