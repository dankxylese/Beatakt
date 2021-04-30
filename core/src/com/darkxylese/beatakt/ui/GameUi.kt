package com.darkxylese.beatakt.ui

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.darkxylese.beatakt.V_HEIGHT_PIXELS
import com.darkxylese.beatakt.V_WIDTH_PIXELS
import ktx.actors.plusAssign
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.scene2d
import kotlin.math.roundToInt

private const val GAME_HUD_LARGE_AREA_WIDTH = 48f
private const val GAME_HUD_SMALL_AREA_WIDTH = 46f
private const val GAME_HUD_BORDER_SIZE_X = 7f
private const val GAME_HUD_AREA_HEIGHT = 9f
private const val GAME_HUD_BORDER_SIZE_Y = 6f
private const val MAX_SCORE = 999999

class GameUi(bundle: I18NBundle) : Group() {
    private val scoreLabel = scene2d.label("0", SkinLabel.DEFAULT.name) {
        width = GAME_HUD_LARGE_AREA_WIDTH
        setAlignment(Align.center)
    }

    private val accuracyLabel = scene2d.label("", SkinLabel.DEFAULT.name) {
        width = GAME_HUD_LARGE_AREA_WIDTH
        setAlignment(Align.center)
    }

    private val streakLabel = scene2d.label("", SkinLabel.DEFAULT.name) {
        width = 5f
        //setAlignment(Align.left)
    }

    private val lifeBarImage = scene2d.image(SkinImage.LIFE_BAR.atlasKey) {
        width = GAME_HUD_SMALL_AREA_WIDTH
        height = GAME_HUD_AREA_HEIGHT
    }


    init {
        var gameHudX: Float
        var gameHudHeight: Float
        var gameHudWidth: Float

        this += scene2d.image(SkinImage.GAME_HUD.atlasKey) {
            gameHudX = V_WIDTH_PIXELS * 0.5f - width * 0.5f
            gameHudHeight = height
            gameHudWidth = width
            x = gameHudX
            y = V_HEIGHT_PIXELS * 0.9f
        }
        this += lifeBarImage.apply {
            setPosition(
                    gameHudX + GAME_HUD_BORDER_SIZE_X,
                    V_HEIGHT_PIXELS * 0.927f
            )
        }

        this += scoreLabel.apply {
            setPosition(
                    gameHudX + gameHudWidth * 0.74f - GAME_HUD_LARGE_AREA_WIDTH * 0.5f,
                    V_HEIGHT_PIXELS * 0.93f

            )
        }

        this += accuracyLabel.apply {
            setPosition(
                    gameHudX + gameHudWidth * 0.5f - GAME_HUD_LARGE_AREA_WIDTH * 0.5f,
                    V_HEIGHT_PIXELS * 0.82f

            )
        }

        this += streakLabel.apply {
            setPosition(
                    2f,
                    V_HEIGHT_PIXELS * 0.01f

            )
        }

    }

    fun updateScore(score: Int) {
        scoreLabel.run {
            text.setLength(0)
            text.append(score)
            invalidateHierarchy()
        }
    }

    fun updateLife(life: Int, maxLife: Int) {
        lifeBarImage.scaleX = MathUtils.clamp(life.toFloat() / (maxLife+1).toFloat(), 0f, 1f)
    }

    fun updateAccu(accu: String) {
        accuracyLabel.run {
            text.setLength(0)
            text.append(accu)
            invalidateHierarchy()
        }
    }

    fun updateStreak(streak: String) {
        streakLabel.run {
            text.setLength(0)
            text.append(streak)
            invalidateHierarchy()
        }
    }


}