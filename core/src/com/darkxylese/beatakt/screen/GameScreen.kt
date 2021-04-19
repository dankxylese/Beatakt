package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.UNIT_SCALE
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger


private val log = logger<GameScreen>()

class GameScreen(game:Beatakt) : BeataktScreen(game) {
    private val viewport = FitViewport(9f, 16f)
    //private val texture = Texture(Gdx.files.internal("images/boxa.png"))
    private val texture = Texture(Gdx.files.internal("images/hitboxUnifiedCentered270a.png"))
    private val sprite = Sprite(texture).apply {
        setSize(9f/(1080/texture.width), 16f/(1920/texture.height))
    }
    private val sprite2 = Sprite(texture).apply {
        setSize(9f/(1080/texture.width), 16f/(1920/texture.height))
    }
    private val sprite3 = Sprite(texture).apply {
        setSize(9f/(1080/texture.width), 16f/(1920/texture.height))
    }
    private val sprite4 = Sprite(texture).apply {
        setSize(9f/(1080/texture.width), 16f/(1920/texture.height))
    }


    override fun show() {
        log.debug { "Game BeataktScreen is Shown" }
        sprite.setPosition((sprite.width) * 0f,2f)
        sprite2.setPosition((sprite.width) * 1f,2f)
        sprite3.setPosition((sprite.width) * 2f,2f)
        sprite4.setPosition((sprite.width) * 3f,2f)

    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        batch.use(viewport.camera.combined) {
            sprite.draw(it)
            sprite2.draw(it)
            sprite3.draw(it)
            sprite4.draw(it)

        }
    }

    override fun dispose() {
        texture.dispose()
    }
}