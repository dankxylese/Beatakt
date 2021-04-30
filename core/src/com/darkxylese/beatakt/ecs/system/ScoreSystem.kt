package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.darkxylese.beatakt.ecs.component.GraphicComponent
import com.darkxylese.beatakt.ecs.component.ScoreComponent
import com.darkxylese.beatakt.event.*
import com.darkxylese.beatakt.screen.MISSES_ALLOWED
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger

private val log = logger<ScoreSystem>()

class ScoreSystem(private val gameEventManager: GameEventManager) : IteratingSystem(allOf(ScoreComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[ScoreComponent.mapper]?.let { score ->
            if (score.missStreak > MISSES_ALLOWED && !score.isDead){
                gameEventManager.dispatchEvent(
                        GameEventType.PLAYER_DEATH,
                        GameEventPlayer.apply {
                            this.hits = score.hits
                            this.s0count = score.s0count
                            this.s50count = score.s50count
                            this.s100count = score.s100count
                            this.s300count = score.s300count
                            this.score = score.score
                            this.bestStreak = score.bestStreak
                        })
                score.isDead = true
                log.debug { "Dispatched Death Event" }
            }
        }
    }
}