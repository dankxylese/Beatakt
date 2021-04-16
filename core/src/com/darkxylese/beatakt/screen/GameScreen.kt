package com.darkxylese.beatakt.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.get
import com.darkxylese.beatakt.ecs.component.*
import com.darkxylese.beatakt.ecs.system.CollisionSystem
import com.darkxylese.beatakt.ecs.system.MoveSystem
import com.darkxylese.beatakt.ecs.system.RenderSystem
import com.darkxylese.beatakt.ecs.system.SpawnSystem
import ktx.app.KtxScreen
import ktx.ashley.*
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(private val batch: Batch,
                 private val font: BitmapFont,
                 private val assets: AssetManager,
                 private val camera: OrthographicCamera,
                 private val engine: PooledEngine) : KtxScreen {

    private val hitbox = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(0f, 144f, 128f, 224f) }
        with<TransformCollisionComponent> { bounds.set(0f, 144f, 60f, 224f) }
        with<MoveComponent>()
        with<RenderComponent> { set(0, true) }
    }
    private val hitboxHa = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(0f, 144f, 128f, 128f) }
        with<RenderComponent>() { z = 2 }
    }
    private val hitboxHb = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(128f, 144f, 128f, 128f) }
        with<RenderComponent>() { z = 2 }
    }
    private val hitboxHc = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(256f, 144f, 128f, 128f) }
        with<RenderComponent>() { z = 2 }
    }
    private val hitboxHd = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(384f, 144f, 128f, 128f) }
        with<RenderComponent>() { z = 2 }
    }


    // create the touchPos to store mouse click position
    private val touchPos = Vector3()

    override fun render(delta: Float) {

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            hitbox[TransformComponent.mapper]?.let { transform -> transform.bounds.x = touchPos.x - 64F}
            hitbox[TransformCollisionComponent.mapper]?.let { transformCol -> transformCol.bounds.x = touchPos.x - 30F}
            //hitbox[TransformComponent.mapper]?.let {transform -> log.debug { transform.bounds.x.toString() }}
            hitbox[RenderComponent.mapper]?.let { render -> render.z = 3 }
            hitbox[RenderComponent.mapper]?.let { render ->
                render.vis = true
                //log.debug { "vis: True"}
            }
        }else{
            hitbox[RenderComponent.mapper]?.let { render -> render.z = -1 } //not visible on screen when background is added on layer 0 or 1
            hitbox[RenderComponent.mapper]?.let { render ->
                render.vis = false
                //log.debug { "vis: False"}
            } //not visible to collision system

        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> hitbox[MoveComponent.mapper]?.let { move -> move.speed.x = -200f }
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> hitbox[MoveComponent.mapper]?.let { move -> move.speed.x = 200f }
            else -> hitbox[MoveComponent.mapper]?.let { move -> move.speed.x = 0f }
        }

        // everything is now done withing our entity engine --> update it every frame

        engine.update(delta)
    }

    override fun show() {
        // start the playback of the background music when we enter game screen
        //assets[MusicAssets.Song].play()

        hitbox[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitRed"))
        hitboxHa[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered"))
        hitboxHb[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered"))
        hitboxHc[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered"))
        hitboxHd[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered"))

        // init entity engine
        engine.apply {
            // add systems
            addSystem(SpawnSystem(hitbox, assets))
            addSystem(MoveSystem())
            addSystem(RenderSystem(hitbox, batch, font, camera))
            // add Collision last since it removes entities
            addSystem(CollisionSystem(hitbox, assets))
        }
    }
}