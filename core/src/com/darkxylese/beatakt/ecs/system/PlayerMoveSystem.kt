package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.darkxylese.beatakt.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.get

class PlayerMoveSystem : IteratingSystem(allOf(TransformComponent::class, PlayerMoveComponent::class, HitCollisionComponent::class).get()) {
    fun update(){

    }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        /*entity[TransformComponent.mapper].let { transform ->
            entity[PlayerMoveComponent.mapper].let { move ->
                // make sure the entities stay within the screen bounds
                transform.bounds.x = MathUtils.clamp(transform.bounds.x + move.speed.x * deltaTime, 0f, 1080f - 135f)
                transform.bounds.y += move.speed.y * deltaTime
            }
        }*/

        entity.moveCmp.run{
            entity[TransformComponent.mapper]?.let { transform ->
                entity[TransformCollisionComponent.mapper]?.let { collisionTransform ->
                    if (order == Move.ONE && cooldown1 == 0f) {
                        transform.bounds.x = 0f
                        collisionTransform.bounds.x = 0f
                        cooldown1 = cooldown
                    }
                    if (order == Move.TWO && cooldown2 == 0f) {
                        transform.bounds.x = 270f
                        collisionTransform.bounds.x = 270f
                        cooldown2 = cooldown
                    }
                    if (order == Move.THREE && cooldown3 == 0f) {
                        transform.bounds.x = 540f
                        collisionTransform.bounds.x = 540f
                        cooldown3 = cooldown
                    }
                    if (order == Move.FOUR && cooldown4 == 0f) {
                        transform.bounds.x = 810f
                        collisionTransform.bounds.x = 810f
                        cooldown4 = cooldown
                    }

                    if (cooldown1 != 0f){
                        cooldown1 -= deltaTime
                    }
                    if (cooldown2 != 0f){
                        cooldown2 -= deltaTime
                    }
                    if (cooldown3 != 0f){
                        cooldown3 -= deltaTime
                    }
                    if (cooldown4 != 0f){
                        cooldown4 -= deltaTime
                    }
                }
            }
        }

    }


}