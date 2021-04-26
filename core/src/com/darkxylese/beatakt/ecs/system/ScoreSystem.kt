package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.darkxylese.beatakt.ecs.component.ScoreComponent
import com.darkxylese.beatakt.event.GameEvent
import com.darkxylese.beatakt.event.GameEventListener
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.event.GameEventType
import ktx.ashley.allOf

class ScoreSystem(gameEventManager: GameEventManager) : GameEventListener, IteratingSystem(allOf(ScoreComponent::class).get()) {


    override fun processEntity(entity: Entity?, deltaTime: Float) {

    }

    override fun onEvent(type: GameEventType, data: GameEvent?) {

    }
}