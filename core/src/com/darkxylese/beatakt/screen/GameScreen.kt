package com.darkxylese.beatakt.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.assets.AssetManager
import com.darkxylese.beatakt.assets.MusicAssets
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.get
import com.darkxylese.beatakt.ecs.component.HitboxComponent
import com.darkxylese.beatakt.ecs.component.MoveComponent
import com.darkxylese.beatakt.ecs.component.RenderComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import com.darkxylese.beatakt.ecs.system.CollisionSystem
import com.darkxylese.beatakt.ecs.system.MoveSystem
import com.darkxylese.beatakt.ecs.system.RenderSystem
import com.darkxylese.beatakt.ecs.system.SpawnSystem
import ktx.app.KtxScreen
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(private val batch: Batch,
                 private val font: BitmapFont,
                 private val assets: AssetManager,
                 private val camera: OrthographicCamera,
                 private val engine: PooledEngine) : KtxScreen {

    private val hitbox = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(0f, 164f, 32f, 64f) }
        with<MoveComponent>()
        with<RenderComponent> { set(0, true) }
    }
    private val hitboxa = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(0f, 100f, 128f, 128f) }
        with<RenderComponent>()
    }
    private val hitboxaa = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(0f, 228f, 128f, 128f) }
        with<RenderComponent>()
    }
    private val hitboxb = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(128f, 100f, 128f, 128f) }
        with<RenderComponent>()
    }
    private val hitboxbb = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(128f, 228f, 128f, 128f) }
        with<RenderComponent>()
    }
    private val hitboxc = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(256f, 100f, 128f, 128f) }
        with<RenderComponent>()
    }
    private val hitboxcc = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(256f, 228f, 128f, 128f) }
        with<RenderComponent>()
    }
    private val hitboxd = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(384f, 100f, 128f, 128f) }
        with<RenderComponent>()
    }
    private val hitboxdd = engine.entity {
        with<HitboxComponent>()
        with<TransformComponent> { bounds.set(384f, 228f, 128f, 128f) }
        with<RenderComponent>()
    }


    // create the touchPos to store mouse click position
    private val touchPos = Vector3()

    override fun render(delta: Float) {

        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            hitbox[TransformComponent.mapper]?.let { transform -> transform.bounds.x = touchPos.x - 32f / 2f }
            hitbox[RenderComponent.mapper]?.let { render -> render.z = 2 }
            hitbox[RenderComponent.mapper]?.let { render ->
                render.vis = true
                log.debug { "vis: True"}
            }
        }else{
            hitbox[RenderComponent.mapper]?.let { render -> render.z = 0 } //not visible on screen
            hitbox[RenderComponent.mapper]?.let { render ->
                render.vis = false
                log.debug { "vis: False"}
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

        hitbox[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("pause"))
        hitboxa[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox2"))
        hitboxaa[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox"))
        hitboxb[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox2"))
        hitboxbb[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox"))
        hitboxc[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox2"))
        hitboxcc[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox"))
        hitboxd[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox2"))
        hitboxdd[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitbox"))

        // init entity engine
        engine.apply {
            // add systems
            addSystem(SpawnSystem(assets))
            addSystem(MoveSystem())
            addSystem(RenderSystem(hitbox, batch, font, camera))
            // add Collision last since it removes entities
            addSystem(CollisionSystem(hitbox, assets))
        }
    }

}