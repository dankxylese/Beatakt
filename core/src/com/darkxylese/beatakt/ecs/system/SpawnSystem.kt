package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.MathUtils
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.get
import com.darkxylese.beatakt.ecs.component.CollisionComponent
import com.darkxylese.beatakt.ecs.component.MoveComponent
import com.darkxylese.beatakt.ecs.component.RenderComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.*

class SpawnSystem(assets: AssetManager) : IntervalSystem(1f) {
    private val hitRegion = assets[TextureAtlasAssets.Game].findRegion("hit")

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
            with<TransformComponent> { bounds.set((MathUtils.random(0, 3))*128f, 910f, 128f, 128f) } //spawns the random hit block
            with<MoveComponent> { speed.set(0f, -400f) } //speed of the hit
            with<CollisionComponent>() //attach collision component to be able to click it
        }
    }
}