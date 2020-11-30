package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.darkxylese.beatakt.assets.SoundAssets
import com.darkxylese.beatakt.assets.get
import com.darkxylese.beatakt.ecs.component.HitboxComponent
import com.darkxylese.beatakt.ecs.component.CollisionComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class CollisionSystem(hitbox: Entity, assets: AssetManager) : IteratingSystem(allOf(TransformComponent::class, CollisionComponent::class).get()) {
    private val hitSound = assets[SoundAssets.Hit]
    private val hitboxBounds = hitbox[TransformComponent.mapper]!!.bounds
    private val hitboxCmp = hitbox[HitboxComponent.mapper]!!

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            if (transform.bounds.y < 0) {
                engine.removeEntity(entity)
            } else if (transform.bounds.overlaps(hitboxBounds)) {
                hitboxCmp.accurateHits++
                hitSound.play()
                engine.removeEntity(entity)
            }
        }
    }
}