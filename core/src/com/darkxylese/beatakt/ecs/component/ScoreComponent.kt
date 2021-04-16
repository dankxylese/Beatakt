package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor
import java.util.*
import kotlin.collections.ArrayList

class ScoreComponent : Component {
    companion object {
        val mapper = mapperFor<ScoreComponent>()
    }

    var hits = 0 //total
    var score = 0
    var accuracy = ""

    var s0count = 0
    var s50count = 0
    var s100count = 0
    var s300count = 0
    var streak = 0
    val currentObjects: Queue<Int> = LinkedList<Int>()

}