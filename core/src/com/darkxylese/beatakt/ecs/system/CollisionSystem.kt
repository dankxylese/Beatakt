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
    private val hitboxCollisionBox = hitbox[TransformCollisionComponent.mapper]!!.bounds //collision box of hitbox
    private val scoreCmp = hitbox[ScoreComponent.mapper]!! //top score thing TEMP


    override fun processEntity(entity: Entity, deltaTime: Float) { //entity here is the falling hit
        entity[RenderComponent.mapper]?.let { render ->
            render.timeSinceCreation += deltaTime

            //calculate time bounds depending on falling rate
            val moveComponentCache = entity[HitMoveComponent.mapper]

            var speed = (if (moveComponentCache?.speed?.y != null) moveComponentCache.speed.y * -0.002 else null) //float of speed (of entity) in the 200 scale # 600 would result in speed = 3

            //TODO: for multiple screen size compatibility, take time to travel the whole screen size, then calculate 3/4 of it, and place the hitbox 1/4 of the screen from the bottom.

            //speed 500 BASELINE TEMPORARY
            val adjustDelay = 0 //TODO: TAKE OUT OF THIS SYSTEM AND ADD TO USER DATA AS A USER CHANGEABLE VARIABLE


            val veryEarly = Vector2()
            veryEarly.x = 2.87F
            veryEarly.y = 3.03F
            val early = Vector2()
            early.x = 3.03F
            early.y = 3.18F
            val perfect = Vector2()
            perfect.x = 3.18F
            perfect.y = 3.32F
            val late = Vector2()
            late.x = 3.32F
            late.y = 3.47F
            val veryLate = Vector2()
            veryLate.x = 3.47F
            veryLate.y = 3.62F




            if (Gdx.input.justTouched()) {
                entity[TransformComponent.mapper]?.let { transform -> //remove when entity goes off screen (with touch)
                    if (transform.bounds.y < 0) {
                        render.timeSinceCreation = 0f //clean time for when entity gets reused
                        scoreCalc(0, "miss")

                        if (popObject(entity)) {   //pop object from tracker
                            engine.removeEntity(entity)
                        }
                    }
                    if (transform.bounds.overlaps(hitboxCollisionBox)) {
                        //log.debug { render.timeSinceCreation.toString() }

                           //add appropriate score if hit
                        if (render.timeSinceCreation >= (veryEarly.x + adjustDelay) / speed!! && render.timeSinceCreation < (veryEarly.y + adjustDelay) / speed){ //very early 50
                            scoreCalc(50, "very early")
                        } else if (render.timeSinceCreation >= (early.x + adjustDelay) / speed && render.timeSinceCreation < (early.y + adjustDelay) / speed){ //early 100
                            scoreCalc(100, "early")
                        } else if (render.timeSinceCreation >= (perfect.x + adjustDelay) / speed && render.timeSinceCreation < (perfect.y + adjustDelay) / speed){ //perfect 300
                            scoreCalc(300, "perfect")
                        } else if (render.timeSinceCreation >= (late.x + adjustDelay) / speed && render.timeSinceCreation < (late.y + adjustDelay) / speed){ //late 100
                            scoreCalc(100, "late")
                        } else if (render.timeSinceCreation >= (veryLate.x + adjustDelay) / speed && render.timeSinceCreation < (veryLate.y + adjustDelay) / speed){ //very late 50
                            scoreCalc(50, "very late")
                        } else { // overlapped too late (or frames were delivered too slowly and not in time (as this is a time dependent and not fps, calculation)) = miss
                            scoreCalc(0, "miss")
                        }

                        scoreCmp.hits++
                        hitSound.play()
                        render.timeSinceCreation = 0f //clean time for when entity gets reused
                        if (popObject(entity)) {
                            engine.removeEntity(entity)
                        }
                    }
                }
            } else {
                entity[TransformComponent.mapper]?.let { transform -> //remove when entity goes off screen (without touch)
                    if (transform.bounds.y < 0) {
                        render.timeSinceCreation = 0f //clean time for when entity gets reused
                        scoreCalc(0, "miss")
                        scoreCmp.currentObjects
                        if (popObject(entity)) {
                            engine.removeEntity(entity)
                        }
                    }
                }
            }
        }
    }

    fun scoreCalc(howAccurateHit : Int, howTimedHit : String){

        // Score = Hit Value + (Hit Value * ((Combo multiplier * Difficulty multiplier * Mod multiplier) / 25))
        if (howAccurateHit == 0) {
            scoreCmp.s0count += 1
            scoreCmp.streak = 0
        }
        if (howAccurateHit == 50) {
            scoreCmp.s50count += 1
            scoreCmp.streak += 1
            scoreCmp.score += 50 + (50 * (scoreCmp.streak)/25) //add difficulty multiplier (based on speed and shiz)
        }
        if (howAccurateHit == 100) {
            scoreCmp.s100count += 1
            scoreCmp.streak += 1
            scoreCmp.score += 100 + (100 * (scoreCmp.streak)/25) //add difficulty multiplier (based on speed and shiz)
        }
        if (howAccurateHit == 300) {
            scoreCmp.s300count += 1
            scoreCmp.streak += 1
            scoreCmp.score += 300 + (300 * (scoreCmp.streak)/25) //add difficulty multiplier (based on speed and shiz)
        }
        scoreCmp.accuracy = howTimedHit
    }
    fun popObject(entity: Entity) : Boolean { //pops the first object from object tracker

        entity[IdComponent.mapper]?.let { ID ->
            //log.debug {ID.id.toString()}
            //log.debug {scoreCmp.currentObjects.peek().toString()}
            return if (scoreCmp.currentObjects.peek() == ID.id){
                scoreCmp.currentObjects.remove()
                //log.debug {"Removed object"}
                true
            } else {
                log.debug {"Failed to remove // BUG"}
                false
            }
        }
        log.debug {"Failed to Return False"}
        return false

    }
}