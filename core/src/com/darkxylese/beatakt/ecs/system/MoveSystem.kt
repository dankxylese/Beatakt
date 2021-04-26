package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.darkxylese.beatakt.V_WIDTH
import com.darkxylese.beatakt.ecs.component.GraphicComponent
import com.darkxylese.beatakt.ecs.component.HitMoveComponent
import com.darkxylese.beatakt.ecs.component.RemoveComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get

private const val UPDATE_RATE = 1/30f

class MoveSystem : IteratingSystem(allOf(TransformComponent::class, HitMoveComponent::class).exclude(RemoveComponent::class).get()) {
    private var accumulator = 0f

    override fun update(deltaTime: Float){
        accumulator += deltaTime
        while(accumulator >= UPDATE_RATE){
            accumulator -= UPDATE_RATE
            entities.forEach { entity ->
                entity[TransformComponent.mapper]?.let{ transform ->
                    transform.prevPos.set(transform.position)
                }
            }
            super.update(UPDATE_RATE)
        }
        val alpha = accumulator / UPDATE_RATE //% between current and next frame
        entities.forEach { entity ->
            entity[TransformComponent.mapper]?.let { transform ->
                transform.interpPos.set(
                        MathUtils.lerp(transform.prevPos.x, transform.position.x, alpha), //linear interpolation
                        MathUtils.lerp(transform.prevPos.y, transform.position.y, alpha),
                        transform.position.z
                )
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform!= null) {"Entity must have a TransformComponent. entity=$entity"}
        val move = entity[HitMoveComponent.mapper]
        require(move!= null) {"Entity must have a HitMoveComponent. entity=$entity"}

        transform.position.x = MathUtils.clamp(transform.position.x, 0f, V_WIDTH - transform.size.x)
        transform.position.y -= move.speed * deltaTime



        if (transform.position.y <= -2f){
            entity.addComponent<RemoveComponent>(engine)
        }
    }
}