package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.darkxylese.beatakt.assets.SoundAsset
import com.darkxylese.beatakt.audio.AudioService
import com.darkxylese.beatakt.ecs.component.*
import com.darkxylese.beatakt.event.GameEventType
import com.darkxylese.beatakt.screen.HITBOX_HEIGHT
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger

private val log = logger<CollisionScoreSystem>()

class CollisionScoreSystem(
        playerHitbox : Entity,
        private val audioService: AudioService
) : IteratingSystem(allOf(TransformComponent::class, CollisionComponent::class).get()){
    //private val playerHit = playerHitbox[TransformCollisionComponent.mapper]!!.bounds //collision box of hitbox
    private val playerCollisionBox = playerHitbox[TransformCollisionComponent.mapper]!!.bounds
    private val scoreCmp = playerHitbox[ScoreComponent.mapper]!!
    private val playerCmp = playerHitbox[PlayerComponent.mapper]!!

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[GraphicComponent.mapper]?.let { graphicCmp ->
            graphicCmp.timeSinceCreation += deltaTime
            val hitMoveCmp = entity[HitMoveComponent.mapper]

            var speed = (if (hitMoveCmp?.speed != null) {
                hitMoveCmp.speed
            } else null)



            val adjustDelay = 0

            val veryEarly = Vector2()
            veryEarly.x = 13F
            veryEarly.y = 13.5F
            val early = Vector2()
            early.x = 13.5F
            early.y = 13.8F
            val perfect = Vector2()
            perfect.x = 13.8F
            perfect.y = 14.2F
            val late = Vector2()
            late.x = 14.2F
            late.y = 14.5F
            val veryLate = Vector2()
            veryLate.x = 14.5F
            veryLate.y = 15F



            if (playerCmp.nextEvent != GameEventType.NONE) {
                //log.debug {"Last Event: "+ playerCmp.nextEvent.toString() }
                entity[TransformComponent.mapper]?.let { transform -> //remove when entity goes off screen (with touch)
                    //log.debug {"Objects on screen " + transform.bounds.toString()}
                    //log.debug {"Player " + playerCollisionBox.toString()}
                    if (transform.position.y < HITBOX_HEIGHT && !transform.belowHitBox){ //as it leaves the hitbox, pop it so that the next hit can be registered with popObject()
                        popOverboardObject(entity)
                        transform.belowHitBox = true
                    }
                    if (transform.position.y < -2f && !transform.removedObject) {
                        graphicCmp.timeSinceCreation = 0f //clean time for when entity gets reused
                        scoreCalc(0, "miss")
                        transform.removedObject = true
                        //playerCmp.nextEvent = GameEventType.NONE
                        //log.debug {"Removed Game Event" }

                        if (popObject(entity)) {   //pop object from tracker
                            entity.addComponent<RemoveComponent>(engine)
                        }
                    }
                    if (transform.bounds.overlaps(playerCollisionBox)) {
                        if (popObject(entity)) {  //finds the next in ID order hit so that higher hits don't get removed first (returns false if not first to be removed)
                        log.debug { graphicCmp.timeSinceCreation.toString() }
                        //log.debug { speed.toString() }

                        //add appropriate score if hit
                        if (graphicCmp.timeSinceCreation < (veryEarly.y + adjustDelay) / speed!!) { //very early 50 speed never null
                            scoreCalc(50, "very early")
                        } else if (graphicCmp.timeSinceCreation >= (early.x + adjustDelay) / speed && graphicCmp.timeSinceCreation < (early.y + adjustDelay) / speed) { //early 100
                            scoreCalc(100, "early")
                        } else if (graphicCmp.timeSinceCreation >= (perfect.x + adjustDelay) / speed && graphicCmp.timeSinceCreation < (perfect.y + adjustDelay) / speed) { //perfect 300
                            scoreCalc(300, "perfect")
                        } else if (graphicCmp.timeSinceCreation >= (late.x + adjustDelay) / speed && graphicCmp.timeSinceCreation < (late.y + adjustDelay) / speed) { //late 100
                            scoreCalc(100, "late")
                        } else if (graphicCmp.timeSinceCreation > (veryLate.x + adjustDelay) / speed) { //very late 50
                            scoreCalc(50, "very late")
                        } //else { // overlapped too late (or frames were delivered too slowly and not in time (as this is a time dependent and not fps, calculation)) = miss
                            //scoreCalc(0, "miss")
                        //}
                        scoreCmp.hits++
                        //hitSound.play()
                        graphicCmp.timeSinceCreation = 0f //clean time for when entity gets reused

                            entity.addComponent<RemoveComponent>(engine)
                            playerCmp.nextEvent = GameEventType.NONE // remove only if when the correct hit block is found. otherwise
                            log.debug {"Removed Game Event" }
                            audioService.play(SoundAsset.HIT, 0.3f)
                        }
                    }
                    if (!transform.bounds.overlaps(playerCollisionBox)){
                        //no overlap
                    }
                }

            } else {
                entity[TransformComponent.mapper]?.let { transform -> //remove when entity goes off screen (without touch)
                    if (transform.position.y < HITBOX_HEIGHT && !transform.belowHitBox){
                        popOverboardObject(entity)
                        transform.belowHitBox = true
                    }
                    if (transform.position.y < -2f && !transform.removedObject) {
                        graphicCmp.timeSinceCreation = 0f //clean time for when entity gets reused
                        scoreCalc(0, "miss")
                        transform.removedObject = true
                        scoreCmp.currentObjects
                        entity.addComponent<RemoveComponent>(engine)
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
            scoreCmp.missStreak += 1
            log.debug { "Missed Hit, Streak = ${scoreCmp.missStreak}, isDead = ${scoreCmp.isDead}" }
        }
        if (howAccurateHit == 50) {
            scoreCmp.s50count += 1
            scoreCmp.streak += 1
            scoreCmp.score += 50 + (50 * (scoreCmp.streak)/25) //add difficulty multiplier (based on speed and shiz)
            scoreCmp.missStreak = 0
        }
        if (howAccurateHit == 100) {
            scoreCmp.s100count += 1
            scoreCmp.streak += 1
            scoreCmp.score += 100 + (100 * (scoreCmp.streak)/25) //add difficulty multiplier (based on speed and shiz)
            scoreCmp.missStreak = 0
        }
        if (howAccurateHit == 300) {
            scoreCmp.s300count += 1
            scoreCmp.streak += 1
            scoreCmp.score += 300 + (300 * (scoreCmp.streak)/25) //add difficulty multiplier (based on speed and shiz)
            scoreCmp.missStreak = 0
        }

        if (scoreCmp.streak > scoreCmp.bestStreak){ //save best streak
            scoreCmp.bestStreak = scoreCmp.streak
        }

        scoreCmp.accuracy = howTimedHit
    }

    fun popObject(entity: Entity) : Boolean { //pops the first object from object tracker

        entity[IdComponent.mapper]?.let { ID ->
            //log.debug {ID.id.toString()}
            //log.debug {scoreCmp.currentObjects.peek().toString()}
            return if (scoreCmp.currentObjects.peek() == ID.id){
                log.debug {"Removed object"}
                true
            } else {
                //log.debug {"Failed to remove // Tried to remove an object out of order"}
                false
            }
        }
        //log.debug {"Failed to Return False in Object Removal"}
        return false

    }

    fun popOverboardObject(entity: Entity) {
        entity[IdComponent.mapper]?.let { ID ->
            scoreCmp.currentObjects.remove()
        }
    }
}