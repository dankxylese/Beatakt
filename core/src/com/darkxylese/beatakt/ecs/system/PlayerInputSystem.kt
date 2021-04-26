package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.darkxylese.beatakt.ecs.component.PlayerComponent
import com.darkxylese.beatakt.ecs.component.RemoveComponent
import com.darkxylese.beatakt.ecs.component.TransformCollisionComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import com.darkxylese.beatakt.event.GameEventManager
import com.darkxylese.beatakt.event.GameEventType
import ktx.ashley.addComponent
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
        require(transform!= null) {"Entity must have a TransformComponent. entity=$entity"}

        touchPosVec.x = Gdx.input.x.toFloat()
        touchPosVecUnprj = gameViewport.unproject(Vector2(touchPosVec))


        //Gdx.input.isKeyPressed(Input.Keys.C)
        if (Gdx.input.isKeyPressed(Input.Keys.C)) {

        }
        if (Gdx.input.isKeyPressed(Input.Keys.V)) {

        }
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {

        }
        if (Gdx.input.isKeyPressed(Input.Keys.N)) {

        }



        if (touchPosVecUnprj.x <= 2.25f){
            if (Gdx.input.justTouched()){
                gameEventManager.dispatchEvent(GameEventType.TOUCH1)
                log.debug { "Touched in section 1" }
            }
            //first tile -> send event to light up row?
        }
        if (touchPosVecUnprj.x in 2.25f..4.5f){
            if (Gdx.input.justTouched()){
                gameEventManager.dispatchEvent(GameEventType.TOUCH2)
                log.debug { "Touched in section 2" }
            }
            //second tile -> send event ^
        }
        if (touchPosVecUnprj.x in 4.5f..6.75f){
            if (Gdx.input.justTouched()){
                gameEventManager.dispatchEvent(GameEventType.TOUCH3)
                log.debug { "Touched in section 3" }
            }
            //third tile -> send event ^
        }
        if (touchPosVecUnprj.x in 6.75f..9f){
            if (Gdx.input.justTouched()){
                gameEventManager.dispatchEvent(GameEventType.TOUCH4)
                log.debug { "Touched in section 4" }
            }
            //fourth tile -> send event ^
        }
        transform.interpPos.x = touchPosVecUnprj.x - 1.125f
        transformCol.bounds.x = transform.interpPos.x
        transformCol.bounds.y = transform.interpPos.y
        //log.debug { "Pos: ${transform.interpPos.x}, ${transform.interpPos.y} // Box: ${transformCol.bounds.x}, ${transformCol.bounds.y}" }
    }
}
