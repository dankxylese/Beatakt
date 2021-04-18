package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.darkxylese.beatakt.ecs.component.MoveComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

private const val UPDATE_RATE = 1/25f

class MoveSystem : IteratingSystem(allOf(TransformComponent::class, MoveComponent::class).get()) {

    private var accumulator = 0f

    override fun update(deltaTime: Float) {
        accumulator += deltaTime
        while(accumulator >= UPDATE_RATE){
            accumulator -= UPDATE_RATE
            super.update(UPDATE_RATE)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[MoveComponent.mapper]?.let { move ->
                // make sure the entities stay within the screen bounds
                transform.bounds.x = MathUtils.clamp(transform.bounds.x + move.speed.x * deltaTime, 0f, 1080f - 135f)
                transform.bounds.y += move.speed.y * deltaTime
            }
        }
    }
}