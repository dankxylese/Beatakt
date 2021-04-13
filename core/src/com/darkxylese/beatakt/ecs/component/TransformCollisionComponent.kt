package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.mapperFor

class TransformCollisionComponent : Component {
    companion object {
        val mapper = mapperFor<TransformCollisionComponent>()
    }

    val bounds = Rectangle()
}