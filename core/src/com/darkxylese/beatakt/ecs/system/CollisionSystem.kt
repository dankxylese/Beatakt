package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Vector2
import com.darkxylese.beatakt.assets.SoundAssets
import com.darkxylese.beatakt.assets.get
import com.darkxylese.beatakt.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger

private val log = logger<CollisionSystem>()

class CollisionSystem(hitbox: Entity, assets: AssetManager) : IteratingSystem(allOf(TransformComponent::class, CollisionComponent::class).get()) {
    private val hitSound = assets[SoundAssets.Hit]
    private val hitboxBounds = hitbox[TransformComponent.mapper]!!.bounds //bounds of touched hitbox
    private val scoreCmp = hitbox[ScoreComponent.mapper]!! //top score thing TEMP


    override fun processEntity(entity: Entity, deltaTime: Float) { //entity here is the falling hit
        entity[RenderComponent.mapper]?.let { render ->
            render.timeSinceCreation += deltaTime

            //calculate time bounds depending on falling rate
            val moveComponentCache = entity[MoveComponent.mapper]

            var speed = (if (moveComponentCache?.speed?.y != null) moveComponentCache.speed.y * -0.005 else null) //float of speed (of entity) in the 200 scale # 600 would result in speed = 3

            //NOTE: for multiple screen size compatibility, take time to travel the whole screen size, then calculate 3/4 of it, and place the hitbox 1/4 of the screen from the bottom.

            //speed 200 BASELINE TEMPORARY
            val veryEarly = Vector2()
            veryEarly.x = 3.52F
            veryEarly.y = 3.66F
            val early = Vector2()
            early.x = 3.66F
            early.y = 3.76F
            val perfect = Vector2()
            perfect.x = 3.76F
            perfect.y = 3.92F
            val late = Vector2()
            late.x = 3.92F
            late.y = 4.04F
            val veryLate = Vector2()
            veryLate.x = 4.04F
            veryLate.y = 4.16F




            if (Gdx.input.isTouched) {
                entity[TransformComponent.mapper]?.let { transform ->
                    if (transform.bounds.y < 0) {
                        render.timeSinceCreation = 0f //clean time for when entity gets reused
                        //log.debug { render.timeSinceCreation.toString() }
                        engine.removeEntity(entity)

                    } else if (transform.bounds.overlaps(hitboxBounds)) {
                        //log.debug { "overlap" }

                           //add appropriate score
                        if (render.timeSinceCreation >= veryEarly.x / speed!! && render.timeSinceCreation < veryEarly.y / speed){ //very early 50
                            scoreCmp.score += 50
                            scoreCmp.accuracy = "very early"
                            log.debug { (render.timeSinceCreation).toString() }
                        }
                        if (render.timeSinceCreation >= early.x / speed && render.timeSinceCreation < early.y / speed){ //early 100
                            scoreCmp.score += 100
                            scoreCmp.accuracy = "early"
                        }
                        if (render.timeSinceCreation >= perfect.x / speed && render.timeSinceCreation < perfect.y / speed){ //perfect 300
                            scoreCmp.score += 300
                            scoreCmp.accuracy = "perfect"
                        }
                        if (render.timeSinceCreation >= late.x / speed && render.timeSinceCreation < late.y / speed){ //late 100
                            scoreCmp.score += 100
                            scoreCmp.accuracy = "late"
                        }
                        if (render.timeSinceCreation >= veryLate.x / speed && render.timeSinceCreation < veryLate.y / speed){ //very late 50
                            scoreCmp.score += 50
                            scoreCmp.accuracy = "very late"
                        }
                        if (render.timeSinceCreation >= veryLate.y / speed ){ //miss
                            scoreCmp.accuracy = "miss"
                        }

                        scoreCmp.hits++
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
                        //log.debug { render.timeSinceCreation.toString() } //should be 0
                        engine.removeEntity(entity)
                    }
                }
            }
        }
    }

    fun scoreCalc(howAccurateHit : Int){

    }
}