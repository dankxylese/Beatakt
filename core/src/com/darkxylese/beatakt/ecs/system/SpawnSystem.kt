package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import com.darkxylese.beatakt.ecs.component.*
import com.darkxylese.beatakt.screen.GameScreen
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.assets.file
import ktx.log.debug
import ktx.log.logger

private val log = logger<SpawnSystem>()

class SpawnSystem(val result: MutableList<Float>) : IntervalSystem( 1/86f) {
    var intervalCounter = 334 //4s to account for speed


    override fun updateInterval() {

        if (result[intervalCounter] > 0f && intervalCounter < result.size-1 && intervalCounter > 7) {
            engine.entity {
                with<TransformComponent> {
                    position.set(MathUtils.random(0, 3) * 2.25f, 16f, 0f)
                }
                with<GraphicComponent> { id = SpriteIDs.HIT }
                with<HitMoveComponent> { speed = 4f }
            }
        }

        intervalCounter++
        //log.debug{intervalCounter.toString() }
    }
}