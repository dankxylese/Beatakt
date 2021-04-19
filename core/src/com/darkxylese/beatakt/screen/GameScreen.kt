package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.UNIT_SCALE
import com.darkxylese.beatakt.ecs.component.GraphicComponent
import com.darkxylese.beatakt.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger


private val log = logger<GameScreen>()

class GameScreen(game:Beatakt) : BeataktScreen(game) {
    //private val texture = Texture(Gdx.files.internal("images/boxa.png"))
    private val hitboxTexture = Texture(Gdx.files.internal("images/hitboxUnifiedCentered270a.png"))

    private val hitboxA = engine.entity {
        with<TransformComponent>{
            position.set(0f,2f,0f)
        }
        with<GraphicComponent>{
            sprite.run{
                setRegion(hitboxTexture)
                setSize(9f/(1080/270), 16f/(1920/270))
            }
        }
    }
    private val hitboxB = engine.entity {
        with<TransformComponent>{
            position.set(2.25f,2f,0f)
        }
        with<GraphicComponent>{
            sprite.run{
                setRegion(hitboxTexture)
                setSize(9f/(1080/270), 16f/(1920/270))
            }
        }
    }
    private val hitboxC = engine.entity {
        with<TransformComponent>{
            position.set(4.5f,2f,0f)
        }
        with<GraphicComponent>{
            sprite.run{
                setRegion(hitboxTexture)
                setSize(9f/(1080/270), 16f/(1920/270))
            }
        }
    }
    private val hitboxD = engine.entity {
        with<TransformComponent>{
            position.set(6.75f,2f,0f)
        }
        with<GraphicComponent>{
            sprite.run{
                setRegion(hitboxTexture)
                setSize(9f/(1080/270), 16f/(1920/270))
            }
        }
    }


    override fun show() {
        log.debug { "Game BeataktScreen is Shown" }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun dispose() {
        hitboxTexture.dispose()
    }
}