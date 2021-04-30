package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class HitMoveComponent : Component, Pool.Poolable {


    var speed = 0f

    override fun reset() {
        speed = 0f
    }


    companion object {
        val mapper = mapperFor<HitMoveComponent>()
    }

}