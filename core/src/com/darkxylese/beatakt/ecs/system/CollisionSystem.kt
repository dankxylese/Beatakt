package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.darkxylese.beatakt.assets.SoundAssets
import com.darkxylese.beatakt.assets.get
import com.darkxylese.beatakt.ecs.component.HitboxComponent
import com.darkxylese.beatakt.ecs.component.CollisionComponent
import com.darkxylese.beatakt.ecs.component.RenderComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import com.darkxylese.beatakt.screen.GameScreen
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger

private val log = logger<CollisionSystem>()

class CollisionSystem(hitbox: Entity, assets: AssetManager) : IteratingSystem(allOf(TransformComponent::class, CollisionComponent::class).get()) {
    private val hitSound = assets[SoundAssets.Hit]
    private val hitboxBounds = hitbox[TransformComponent.mapper]!!.bounds //bounds of touch
    private val hitboxCmp = hitbox[HitboxComponent.mapper]!!


    override fun processEntity(entity: Entity, deltaTime: Float) { //entity here is the falling hit
        entity[RenderComponent.mapper]?.let { render ->
            render.timeSinceCreation += deltaTime
            if (Gdx.input.isTouched) {
                entity[TransformComponent.mapper]?.let { transform ->
                    if (transform.bounds.y < 0) {
                        render.timeSinceCreation = 0f //clean time for when entity gets reused
                        log.debug { render.timeSinceCreation.toString() }
                        engine.removeEntity(entity)

                    } else if (transform.bounds.overlaps(hitboxBounds)) {
                        //log.debug { "overlap" }

                           //add appropriate score
                        if (render.timeSinceCreation >= 1.705 && render.timeSinceCreation < 1.765){ //very early 50
                            hitboxCmp.score += 50
                            hitboxCmp.accuracy = "very early"
                        }
                        if (render.timeSinceCreation >= 1.765 && render.timeSinceCreation < 1.825){ //early 100
                            hitboxCmp.score += 100
                            hitboxCmp.accuracy = "early"
                        }
                        if (render.timeSinceCreation >= 1.825 && render.timeSinceCreation < 1.905){ //perfect 300
                            hitboxCmp.score += 300
                            hitboxCmp.accuracy = "perfect"
                        }
                        if (render.timeSinceCreation >= 1.905 && render.timeSinceCreation < 1.965){ //late 100
                            hitboxCmp.score += 100
                            hitboxCmp.accuracy = "late"
                        }
                        if (render.timeSinceCreation >= 1.965 && render.timeSinceCreation < 2.025){ //very late 50
                            hitboxCmp.score += 50
                            hitboxCmp.accuracy = "very late"
                        }

                        hitboxCmp.hits++
                        hitSound.play()
                        render.timeSinceCreation = 0f //clean time for when entity gets reused
                        engine.removeEntity(entity)
                    }
                }
            } else {
                //log.debug { "no overlap" }
                entity[TransformComponent.mapper]?.let { transform ->
                    if (transform.bounds.y < 0) {
                        render.timeSinceCreation = 0f //clean time for when entity gets reused
                        log.debug { render.timeSinceCreation.toString() }
                        engine.removeEntity(entity)
                    }
                }
            }
        }
    }
}