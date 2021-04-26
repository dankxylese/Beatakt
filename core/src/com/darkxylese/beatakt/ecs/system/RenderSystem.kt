package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
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
import java.util.*


private val log = logger<RenderSystem>()

class RenderSystem(
        private val batch: Batch,
        private val gameViewport: Viewport,
        private val uiViewport: Viewport,
        background1Texture: Texture,
        background2Texture: Texture
) :SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).exclude(RemoveComponent::class).get(),
        compareBy { entity -> entity[TransformComponent.mapper] }
){
    var timeSinceCreation = 0f
    private val background1ScrollSpeed = Vector2(0f, -0.02f)
    private val background2ScrollSpeed = Vector2(0f, -0.07f)


    private val background1 = Sprite(background1Texture.apply{
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    })

    private val background2 = Sprite(background2Texture.apply{
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    })


    override fun update(deltaTime: Float) {
        //Render Background
        uiViewport.apply()
        batch.use(uiViewport.camera.combined){
            background1.run {
                scroll(background1ScrollSpeed.x*deltaTime, background1ScrollSpeed.y*deltaTime)
                //setAlpha(0.2f) //between 0 and 1 / 0 being black
                draw(batch)
            }
            background2.run {
                scroll(background2ScrollSpeed.x*deltaTime, background2ScrollSpeed.y*deltaTime)
                draw(batch)
            }
        }

        //Render Game Objects
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