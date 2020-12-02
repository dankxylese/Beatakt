package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import ktx.ashley.mapperFor

class RenderComponent : Component {
    companion object {
        val mapper = mapperFor<RenderComponent>()
    }

    val sprite = Sprite()
    // Determines which object overlaps what
    var z = 1 //render layering priority
    var vis = true //visible to collision
    var timeSinceCreation = 0f

    fun set(z: Int, vis: Boolean) {
        this.z = z
        this.vis = vis
    }
}