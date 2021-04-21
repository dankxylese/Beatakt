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
            super.update(UPDATE_RATE)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform!= null) {"Entity must have a TransformComponent. entity=$entity"}
        val move = entity[HitMoveComponent.mapper]
        require(move!= null) {"Entity must have a HitMoveComponent. entity=$entity"}

        transform.position.x = MathUtils.clamp(transform.position.x, 0f, V_WIDTH - transform.size.x)
        transform.position.y -= move.speed * deltaTime



        if (transform.position.y <= -0.7f){
            entity.addComponent<RemoveComponent>(engine)
        }
    }
}