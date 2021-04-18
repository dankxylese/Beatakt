package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.darkxylese.beatakt.ecs.component.Move
import com.darkxylese.beatakt.ecs.component.PlayerMoveComponent
import com.darkxylese.beatakt.ecs.component.moveCmp
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.event.Key
import com.darkxylese.beatakt.input.InputListener
import ktx.ashley.allOf


class PlayerInputSystem(private val gameEventManager: GameEventManager, engine: Engine) :
        EntitySystem(), InputListener {

    private val entities = engine.getEntitiesFor(allOf(PlayerMoveComponent::class).get())

    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addInputListener(this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeInputListener(this)
        super.removedFromEngine(engine)
    }

    override fun keyPressed(key: Key) {
        when (key) {
            Key.ONE -> entities.forEach {
                with(it.moveCmp) { //TODO: [ALL] might need to check if it was already moved in the current engine tick
                    order = Move.ONE
                }
            }
            Key.TWO -> entities.forEach {
                with(it.moveCmp) {
                    order = Move.TWO
                }
            }
            Key.THREE -> entities.forEach {
                with(it.moveCmp) {
                    order = Move.THREE
                }
            }
            Key.FOUR -> entities.forEach {
                with(it.moveCmp) {
                    order = Move.FOUR
                }
            }
            Key.NONE -> entities.forEach{
                with(it.moveCmp) {
                    order = Move.NONE
                }
            }
        }
    }

    override fun keyReleased(key: Key) {
        when (key) {
            Key.ONE -> entities.forEach { it.moveCmp.order = Move.NONE }
            else -> {
            }
        }
    }
}