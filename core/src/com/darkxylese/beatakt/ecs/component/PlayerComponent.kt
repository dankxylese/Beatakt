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


    override fun reset() {
        nextEvent = GameEventType.NONE
        var touchEnabled = true
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}