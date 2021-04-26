package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class IdComponent : Component {
    var id = 0

    companion object {
        val mapper = mapperFor<IdComponent>()
    }
}