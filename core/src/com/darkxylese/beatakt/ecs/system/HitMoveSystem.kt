package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.darkxylese.beatakt.ecs.component.HitMoveComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MoveSystem : IteratingSystem(allOf(TransformComponent::class, HitMoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[HitMoveComponent.mapper]?.let { move ->
                // make sure the entities stay within the screen bounds
                transform.bounds.x = MathUtils.clamp(transform.bounds.x + move.speed.x * deltaTime, 0f, 1080f - 135f)
                transform.bounds.y += move.speed.y * deltaTime
            }
        }
    }
}