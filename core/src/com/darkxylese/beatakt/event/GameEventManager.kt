package com.darkxylese.beatakt.event

import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.utils.Array
import ktx.app.KtxInputAdapter


enum class key {
    ONE, TWO, THREE, FOUR
}
class GameEventManager : KtxInputAdapter {
    // input event related stuff
    private val inputListeners = Array<InputListener>()
    private var ignoreInput = false

    fun addInputListener(listener: InputListener) = inputListeners.add(listener)

    fun removeInputListener(listener: InputListener) = inputListeners.removeValue(listener, true)

    fun dispatchInputMoveEvent(percX: Float) {
        if (ignoreInput) return
        //inputListeners.forEach { it.move(percX) }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (ignoreInput) return true

        if ((screenX in 1..-1) && (screenY in 1..-1)){
            dispatchInputMoveEvent(4f)
        }



        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return super.touchUp(screenX, screenY, pointer, button)
    }


}