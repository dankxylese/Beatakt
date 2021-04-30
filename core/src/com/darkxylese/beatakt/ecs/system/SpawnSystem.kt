package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.math.MathUtils
import com.darkxylese.beatakt.ecs.component.*
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.event.GameEventPlayer
import com.darkxylese.beatakt.event.GameEventType
import com.darkxylese.beatakt.screen.SPAWN_SPEED
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger

private val log = logger<SpawnSystem>()

class SpawnSystem(
        private val result: MutableList<Float>,
        private val gameEventManager: GameEventManager,
        playerHitbox : Entity,
        inter: Float,
        intervalCounterAhead: Int
        ) : IntervalSystem(inter) {
    var intervalCounter = 0+intervalCounterAhead //4s to account for speed
    var createdTotal = 0
    var finished = false
    private val scoreCmp = playerHitbox[ScoreComponent.mapper]!! //top score thing TEMP



    override fun updateInterval() {
        val randPosX = MathUtils.random(0, 3) * 2.25f
        if (intervalCounter < (result.size-1) && result[intervalCounter] > 0f) { //check (if statement) in this order or the result gets called out of bounds first
            engine.entity {
                with<TransformComponent> {
                    setInitPos(randPosX, 16f, 0f)
                    //bounds.set(randPosX, 16f, 9f/(1080/270), 16f/(1920/270))
                }
                with<CollisionComponent>()
                with<GraphicComponent> { id = SpriteIDs.HIT }
                with<HitMoveComponent> { speed = SPAWN_SPEED }
                with<IdComponent>{id = createdTotal}
            }
            scoreCmp.currentObjects.add(createdTotal)
            createdTotal+=1
        }

        if (intervalCounter > result.size && !finished){
            gameEventManager.dispatchEvent(
                GameEventType.ENDGAME,
                GameEventPlayer.apply {
                    this.hits = scoreCmp.hits
                    this.s0count = scoreCmp.s0count
                    this.s50count = scoreCmp.s50count
                    this.s100count = scoreCmp.s100count
                    this.s300count = scoreCmp.s300count
                    this.score = scoreCmp.score
                    this.bestStreak = scoreCmp.bestStreak
                })
            log.debug { "Dispatched ENDGAME Event" }
            finished = true
        }
        intervalCounter++
        //log.debug{intervalCounter.toString() }
    }
}