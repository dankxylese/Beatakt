package com.darkxylese.beatakt.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import com.darkxylese.beatakt.assets.TextureAtlasAssets
import com.darkxylese.beatakt.assets.get
import com.darkxylese.beatakt.ecs.component.*
import com.darkxylese.beatakt.ecs.system.*
import com.darkxylese.beatakt.event.GameEventManager
import ktx.app.KtxScreen
import ktx.ashley.*
import ktx.log.logger

private val log = logger<GameScreen>()

class GameScreen(private val batch: Batch,
                 private val font: BitmapFont,
                 private val assets: AssetManager,
                 private val camera: OrthographicCamera,
                 private val engine: PooledEngine,
                 private val gameEventManager: GameEventManager) : KtxScreen {


    private val hitbox = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(0f, 304f, 270f, 473f) }
        with<TransformCollisionComponent> { bounds.set(0f, 304f, 200f, 224f) }
        with<PlayerMoveComponent>()
        with<RenderComponent> { set(0, true) }
    }
    private val hitboxHa = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(0f, 304f, 270f, 270f) }
        with<RenderComponent>() { z = 2 }
    }
    private val hitboxHb = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(270f, 304f, 270f, 270f) }
        with<RenderComponent>() { z = 2 }
    }
    private val hitboxHc = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(540f, 304f, 270f, 270f) }
        with<RenderComponent>() { z = 2 }
    }
    private val hitboxHd = engine.entity {
        with<ScoreComponent>()
        with<TransformComponent> { bounds.set(810f, 304f, 270f, 270f) }
        with<RenderComponent>() { z = 2 }
    }


    // create the touchPos to store mouse click position
    private val touchPos = Vector3()

    override fun render(delta: Float) {
        font.data.setScale(2.5f)

        /*
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                log.debug{"X: $screenX, Y: $screenY"}

                return true
            }
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            hitbox[TransformComponent.mapper]?.let { transform -> transform.bounds.x = touchPos.x - 64F}
            hitbox[TransformCollisionComponent.mapper]?.let { transformCol -> transformCol.bounds.x = touchPos.x - 30F}
            hitbox[TransformComponent.mapper]?.let {transform -> log.debug { transform.bounds.x.toString() }}
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

        } */

        // everything is now done withing our entity engine --> update it every frame

        engine.update(delta)
    }

    override fun show() {
        // start the playback of the background music when we enter game screen
        //assets[MusicAssets.Song].play()
        gameEventManager.addInputListener(this)

        hitbox[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitRed270"))
        hitboxHa[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered270"))
        hitboxHb[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered270"))
        hitboxHc[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered270"))
        hitboxHd[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("hitboxUnifiedCentered270"))

        // init entity engine
        engine.apply {
            // add this screen specific systems
            addSystem(SpawnSystem(hitbox, assets))
            addSystem(RenderSystem(hitbox, batch, font, camera))
            // add Collision last since it removes entities
            addSystem(CollisionSystem(hitbox, assets))
        }
    }
}