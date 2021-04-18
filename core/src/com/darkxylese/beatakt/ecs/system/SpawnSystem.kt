package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.MathUtils
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.get
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.darkxylese.beatakt.ecs.component.*
import ktx.ashley.*

class SpawnSystem(hitbox: Entity, assets: AssetManager) : IntervalSystem(MathUtils.random(0.5f, 0.9f)) { //1f = 1s TODO: Remove interval system, add map reading system, which will call spawn whenever its needed.
    private val hitRegion = assets[TextureAtlasAssets.Game].findRegion("hit270")
    private var createdTotal = 0
    private val scoreCmp = hitbox[ScoreComponent.mapper]!! //top score thing TEMP

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        // spawn an initial hit when the system is added to the engine
        updateInterval()
    }

    override fun updateInterval() {
        engine.entity {
            with<RenderComponent> {
                sprite.setRegion(hitRegion)
                z = 2
            }
            with<TransformComponent> { bounds.set((MathUtils.random(0, 3))*270f, 1920f, 270f, 270f) } //spawns the random hit block
            with<MoveComponent> { speed.set(0f, -1000f) } //speed of the hit
            with<CollisionComponent>() //attach collision component to be able to click it
            with<IdComponent> { id = createdTotal }
        }
        scoreCmp.currentObjects.add(createdTotal)
        createdTotal+=1
    }
}


