package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.darkxylese.beatakt.ecs.component.PlayerComponent
import com.darkxylese.beatakt.ecs.component.TransformCollisionComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.event.GameEventType
import com.darkxylese.beatakt.screen.INPUT_TIMEOUT
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger

private val log = logger<PlayerInputSystem>()

class PlayerInputSystem(
        private val gameViewport: Viewport,
        private val gameEventManager: GameEventManager
) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class).get()) {
    private val touchPosVec = Vector2()
    private var touchPosVecUnprj = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        val transformCol = entity[TransformCollisionComponent.mapper]!!
        val playerCmp = entity[PlayerComponent.mapper]!!
        require(transform!= null) {"Entity must have a TransformComponent. entity=$entity"}

        playerCmp.timeSinceEvent+=deltaTime

        if (playerCmp.timeSinceEvent > INPUT_TIMEOUT){
            playerCmp.timeSinceEvent = 0f
            playerCmp.nextEvent = GameEventType.NONE
        }



        if (playerCmp.touchEnabled){
            touchPosVec.x = Gdx.input.x.toFloat()
            touchPosVecUnprj = gameViewport.unproject(Vector2(touchPosVec))
        }


        if (Gdx.input.justTouched()){
            playerCmp.touchEnabled = true
        }

        //Gdx.input.isKeyPressed(Input.Keys.C)
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            playerCmp.touchEnabled = false
            transform.interpPos.x = 0f
            touchPosVecUnprj.x = 10f
            updateCollBox(transformCol, 0f)
            playerCmp.nextEvent = (GameEventType.TOUCH1)
            gameEventManager.dispatchEvent(GameEventType.TOUCH1)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            playerCmp.touchEnabled = false
            transform.interpPos.x = 2.25f
            touchPosVecUnprj.x = 10f
            updateCollBox(transformCol, 2.25f)
            playerCmp.nextEvent = (GameEventType.TOUCH2)
            gameEventManager.dispatchEvent(GameEventType.TOUCH2)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            playerCmp.touchEnabled = false
            transform.interpPos.x = 4.5f
            touchPosVecUnprj.x = 10f
            updateCollBox(transformCol, 4.5f)
            playerCmp.nextEvent = (GameEventType.TOUCH3)
            gameEventManager.dispatchEvent(GameEventType.TOUCH3)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            playerCmp.touchEnabled = false
            transform.interpPos.x = 6.75f
            touchPosVecUnprj.x = 10f
            updateCollBox(transformCol, 6.75f)
            playerCmp.nextEvent = (GameEventType.TOUCH4)
            gameEventManager.dispatchEvent(GameEventType.TOUCH4)
        }



        if (touchPosVecUnprj.x <= 2.25f){
            if (Gdx.input.justTouched()){
                updateCollBox(transformCol, touchPosVecUnprj.x)
                playerCmp.nextEvent = (GameEventType.TOUCH1)
                gameEventManager.dispatchEvent(GameEventType.TOUCH1) //for animation
            }
            transform.interpPos.x = touchPosVecUnprj.x - 1.125f
        }
        if (touchPosVecUnprj.x in 2.25f..4.5f){
            if (Gdx.input.justTouched()){
                updateCollBox(transformCol, touchPosVecUnprj.x)
                playerCmp.nextEvent = (GameEventType.TOUCH2)
                gameEventManager.dispatchEvent(GameEventType.TOUCH2) //for animation
            }
            transform.interpPos.x = touchPosVecUnprj.x - 1.125f
        }
        if (touchPosVecUnprj.x in 4.5f..6.75f){
            if (Gdx.input.justTouched()){
                updateCollBox(transformCol, touchPosVecUnprj.x)
                playerCmp.nextEvent = (GameEventType.TOUCH3)
                gameEventManager.dispatchEvent(GameEventType.TOUCH3) //for animation
            }
            transform.interpPos.x = touchPosVecUnprj.x - 1.125f
        }
        if (touchPosVecUnprj.x in 6.75f..9f){
            if (Gdx.input.justTouched()){
                updateCollBox(transformCol, touchPosVecUnprj.x)
                playerCmp.nextEvent = (GameEventType.TOUCH4)
                gameEventManager.dispatchEvent(GameEventType.TOUCH4) //for animation
            }
            transform.interpPos.x = touchPosVecUnprj.x - 1.125f
        }
         //Updates the visual representation of the player hit
        //log.debug { "Pos: ${transform.interpPos.x}, ${transform.interpPos.y} // Box: ${transformCol.bounds.x}, ${transformCol.bounds.y}" }
    }
    fun updateCollBox(transformCol: TransformCollisionComponent, newPos: Float) { //important to update the collision box before we send an event
        transformCol.bounds.x = newPos
    }
}
