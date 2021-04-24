package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.darkxylese.beatakt.ecs.component.GraphicComponent
import com.darkxylese.beatakt.ecs.component.RemoveComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.debug
import ktx.log.error
import ktx.log.logger


private val log = logger<RenderSystem>()

class RenderSystem(
        private val batch: Batch,
        private val gameViewport: Viewport
) :SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).exclude(RemoveComponent::class).get(),
        compareBy { entity -> entity[TransformComponent.mapper] }
){
    var timeSinceCreation = 0f
    override fun update(deltaTime: Float) {

        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined){
            super.update(deltaTime)
        }
        //timeSinceCreation += deltaTime
        //log.debug { "Time since creation $timeSinceCreation" }
    }


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {"Entity must have a TransformComponent. entity=$entity"}
        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) {"Entity must have a GraphicComponent. entity=$entity"}

        if(graphic.sprite.texture == null) {
            log.error{"Entity has no texture for rendering. entity=$entity"}
            return
        }

        graphic.sprite.run{
            setBounds(transform.interpPos.x, transform.interpPos.y, transform.size.x, transform.size.y)
            draw(batch)
        }

    }
}