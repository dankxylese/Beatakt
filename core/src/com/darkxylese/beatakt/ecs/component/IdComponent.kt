package com.darkxylese.beatakt.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor


class IdComponent : Component {
    companion object {
        val mapper = mapperFor<IdComponent>()
    }

    var id = 0


}