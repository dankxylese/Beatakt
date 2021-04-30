package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class SpriteIDs {
    NONE,
    HITBOX,
    PLAYER,
    HIT
}

class GraphicComponent : Component, Pool.Poolable{
    val sprite = Sprite()
    var id = SpriteIDs.NONE
    var timeSinceCreation = 0f

    override fun reset() {
        id = SpriteIDs.NONE
        sprite.texture = null
        sprite.setColor(1f,1f,1f,1f)
        timeSinceCreation = 0f
    }

    fun setSpriteRegion(region: TextureRegion){
        sprite.run{
            setRegion(region)
            setSize(9f/(1080/texture.width), 16f/(1920/texture.height))
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}