package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.darkxylese.beatakt.ecs.component.GraphicComponent
import com.darkxylese.beatakt.ecs.component.PlayerComponent
import com.darkxylese.beatakt.ecs.component.SpriteIDs
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class TextureSystem(
        private val hitboxBorderRegion: TextureRegion,
        private val playerHitboxRegion: TextureRegion,
        private val hitRegion: TextureRegion,
) : IteratingSystem(allOf(GraphicComponent::class).get()), EntityListener {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }


    override fun entityAdded(entity: Entity) {
        //entity[GraphicComponent.mapper]?.setSpriteRegion(hitboxBorderRegion) //temporary testure
    }

    override fun entityRemoved(entity: Entity?) = Unit


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val graphic = entity[GraphicComponent.mapper]
        require(graphic!= null) {"Entity must have a GraphicComponent. entity=$entity"}

        //val region = defaultRegion
        if (graphic.id == SpriteIDs.HITBOX){
            graphic.setSpriteRegion(hitboxBorderRegion)
        }
        if (graphic.id == SpriteIDs.PLAYER){
            graphic.setSpriteRegion(playerHitboxRegion)
        }
        if (graphic.id == SpriteIDs.HIT){
            graphic.setSpriteRegion(hitRegion)
        }

    }
}