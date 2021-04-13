package com.darkxylese.beatakt.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.darkxylese.beatakt.ecs.component.ScoreComponent
import com.darkxylese.beatakt.ecs.component.RenderComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class RenderSystem(hitbox: Entity,
                   private val batch: Batch,
                   private val font: BitmapFont,
                   private val camera: OrthographicCamera) : SortedIteratingSystem(
        allOf(TransformComponent::class, RenderComponent::class).get(),
        // compareBy is used to render entities by their z-index
        compareBy { entity: Entity -> entity[RenderComponent.mapper]?.z }) {
    private val hitboxCmp = hitbox[ScoreComponent.mapper]!!

    override fun update(deltaTime: Float) {
        forceSort()
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        batch.projectionMatrix = camera.combined
        // draw all entities in one batch
        batch.use {
            super.update(deltaTime)
            font.draw(batch, "Hits: ${hitboxCmp.hits}  ｜  Score: ${hitboxCmp.score}  ｜  Accuracy: ${hitboxCmp.accuracy}", 0f, 910f)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[RenderComponent.mapper]?.let { render ->
                batch.draw(render.sprite, transform.bounds.x, transform.bounds.y)
            }
        }
    }
}