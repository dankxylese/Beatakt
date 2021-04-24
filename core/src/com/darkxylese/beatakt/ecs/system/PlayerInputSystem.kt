package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.darkxylese.beatakt.ecs.component.PlayerComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import com.darkxylese.beatakt.screen.GameScreen
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger

private val log = logger<PlayerInputSystem>()

class PlayerInputSystem(
        private val gameViewport: Viewport
) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class).get()) {
    private val touchPosVec = Vector2()
    private var touchPosVecUnprj = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform!= null) {"Entity must have a TransformComponent. entity=$entity"}

        touchPosVec.x = Gdx.input.x.toFloat()
        touchPosVecUnprj = gameViewport.unproject(Vector2(touchPosVec))


        if (touchPosVecUnprj.x <= 2.25f){
            //first tile -> send event to light up row?
        }
        if (touchPosVecUnprj.x in 2.25f..4.5f){
            //second tile -> send event ^
        }
        if (touchPosVecUnprj.x in 4.5f..6.75f){
            //third tile -> send event ^
        }
        if (touchPosVecUnprj.x in 6.75f..9f){
            //fourth tile -> send event ^
        }
        transform.interpPos.x = touchPosVecUnprj.x - 1.125f
        //log.debug { "Raw: ${touchPosVec.x}, ${touchPosVec.y} // Unprojected: ${touchPosVecUnprj.x}, ${touchPosVecUnprj.y}" }
    }
}