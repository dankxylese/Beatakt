package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.darkxylese.beatakt.event.GameEventType
import javazoom.jl.player.Player
import ktx.ashley.mapperFor
import java.util.*

class PlayerComponent : Component, Pool.Poolable{

    var nextEvent: GameEventType = GameEventType.NONE
    var touchEnabled = true
    var timeSinceEvent = 0f


    override fun reset() {
        nextEvent = GameEventType.NONE
        touchEnabled = true
        timeSinceEvent = 0f
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}