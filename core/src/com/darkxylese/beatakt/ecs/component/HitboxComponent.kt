package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class HitboxComponent : Component {
    companion object {
        val mapper = mapperFor<HitboxComponent>()
    }

    var accurateHits = 0
}