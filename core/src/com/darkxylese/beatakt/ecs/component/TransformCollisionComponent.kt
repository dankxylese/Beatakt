package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class TransformCollisionComponent : Component, Pool.Poolable{

    val bounds = Rectangle() //our collision box


    override fun reset() {
        bounds.set(0f,0f,0f,0f)
    }

    fun setInitBox(x: Float, y: Float, w: Float, h: Float) {
        bounds.set(x,y,w,h)

    }


    companion object {
        val mapper = mapperFor<TransformCollisionComponent>()
    }
}