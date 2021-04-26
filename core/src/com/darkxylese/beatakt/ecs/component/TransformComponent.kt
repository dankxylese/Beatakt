package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent>{
    val position = Vector3()
    val prevPos = Vector3()
    val interpPos = Vector3()
    var belowHitBox = false
    val size = Vector2(9f/(1080/270), 16f/(1920/270)) // 9f/(1080/texture.width), 16f/(1920/texture.height) 270x270
    var bounds = Rectangle()


    override fun reset() {
        position.set(Vector3.Zero)
        prevPos.set(Vector3.Zero)
        interpPos.set(Vector3.Zero)
        size.set(9f/(1080/270), 16f/(1920/270))
        bounds = Rectangle(0f,0f,0f,0f)
        belowHitBox = false
    }

    fun setInitPos(x: Float, y: Float, z: Float) {
        position.set(x,y,z)
        prevPos.set(x,y,z)
        interpPos.set(x,y,z)
        bounds.set(x,y,9f/(1080/270), 16f/(1920/270))
    }

    override fun compareTo(other: TransformComponent): Int {
        val zDiff = position.z.compareTo(other.position.z)
        return if (zDiff == 0) position.y.compareTo(other.position.y) else zDiff
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }



}