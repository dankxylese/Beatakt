package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.UNIT_SCALE
import com.darkxylese.beatakt.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import java.lang.Float.min


private val log = logger<GameScreen>()
private const val MAX_DELTA_TIME = 1/30f

class GameScreen(game:Beatakt) : BeataktScreen(game) {
    //private val texture = Texture(Gdx.files.internal("images/boxa.png"))

    private val playerHitbox = engine.entity {
        with<TransformComponent>{
            position.set(0f,2f,2f)
        }
        with<PlayerComponent>()
        with<GraphicComponent>{id=SpriteIDs.PLAYER}
    }

    private val hit = engine.entity {
        with<TransformComponent>{

            position.set(2.25f,10f,0f)
        }
        with<GraphicComponent>{id=SpriteIDs.HIT}
        with<HitMoveComponent> { speed = 2f}
    }
    private val hitboxA = engine.entity {
        with<TransformComponent>{
            position.set(0f,2f,1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxB = engine.entity {
        with<TransformComponent>{
            position.set(2.25f,2f,1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxC = engine.entity {
        with<TransformComponent>{
            position.set(4.5f,2f,1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxD = engine.entity {
        with<TransformComponent>{
            position.set(6.75f,2f,1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }


    override fun show() {
        log.debug { "Game BeataktScreen is Shown" }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }

}