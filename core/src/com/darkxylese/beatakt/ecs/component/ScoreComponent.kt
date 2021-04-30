package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import java.util.*


class ScoreComponent : Component, Pool.Poolable {
    var hits = 0 //total
    var score = 0
    var accuracy = ""
    var beatMapName = ""
    var length = 0f
    var beatMapLoc: FileHandle? = null
    var beatSongLoc: FileHandle? = null
    var isDead = false

    var s0count = 0
    var s50count = 0
    var s100count = 0
    var s300count = 0
    var streak = 0
    var bestStreak = 0
    var missStreak = 0
    var currentObjects: Queue<Int> = LinkedList<Int>()

    override fun reset() {
        hits = 0
        score = 0
        accuracy = ""
        s0count = 0
        s50count = 0
        s100count = 0
        s300count = 0
        streak = 0
        beatMapName = ""
        length = 0f
        missStreak = 0
        beatMapLoc = null
        beatSongLoc = null
        isDead = false
        currentObjects.clear()
    }

    companion object {
        val mapper = mapperFor<ScoreComponent>()
    }

}