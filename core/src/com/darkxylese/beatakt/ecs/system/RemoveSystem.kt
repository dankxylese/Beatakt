package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.darkxylese.beatakt.ecs.component.RemoveComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RemoveSystem : IteratingSystem(allOf(RemoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val remove = entity[RemoveComponent.mapper]
        require(remove!= null) {"Entity must have a RemoveComponent. entity=$entity"}

        remove.delay -= deltaTime
        if(remove.delay <= 0f){
            engine.removeEntity(entity)
        }
    }
}