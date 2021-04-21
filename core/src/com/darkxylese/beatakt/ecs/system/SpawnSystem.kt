package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.Gdx

class SpawnSystem : IntervalSystem(1/30f) {
    var intervalCounter = 0
    val temp = Gdx.files.local("temp.txt")


    override fun updateInterval() {

    }

}