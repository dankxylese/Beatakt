package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import ktx.ashley.mapperFor

class HitMoveComponent : Component {
    companion object {
        val mapper = mapperFor<HitMoveComponent>()
    }

    val speed = Vector2() //holder for speed of an object
}