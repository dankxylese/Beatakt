package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import ktx.ashley.mapperFor

enum class Move {
    NONE, ONE, TWO, THREE, FOUR
}

class PlayerMoveComponent : Component {
    companion object {
        val mapper = mapperFor<PlayerMoveComponent>()
    }
    var order = Move.NONE
    val cooldown = 0.2f
    var cooldown1 = 0f
    var cooldown2 = 0f
    var cooldown3 = 0f
    var cooldown4 = 0f

}

val Entity.moveCmp: PlayerMoveComponent
    get() = this[PlayerMoveComponent.mapper]
            ?: throw KotlinNullPointerException("Trying to access an ability component which is null")
