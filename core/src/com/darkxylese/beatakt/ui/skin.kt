package com.darkxylese.beatakt.ui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.darkxylese.beatakt.assets.BitmapFontAsset
import com.darkxylese.beatakt.assets.TextureAtlasAsset
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin

enum class SkinLabel {
    LARGE, DEFAULT
}

enum class SkinImage(val atlasKey: String) {
    GAME_HUD("game_hud"),
    WARNING("warning"),
    LIFE_BAR("life_bar"),
    PLAY("play"),
    PAUSE("pause"),
    QUIT("quit"),
    FRAME("frame"),
    FRAME_TRANSPARENT("frame_transparent"),
    SOUND_ON("sound"),
    SOUND_OFF("no_sound"),
    SCROLL_V("scroll_v"),
    SCROLL_KNOB("scroll_knob"),
    FRAME_LABEL("label_frame"),
    FRAME_LABEL_TRANSPARENT("label_frame_transparent")
}

fun createSkin(assets: AssetStorage){
    val atlas = assets[TextureAtlasAsset.UI.descriptor]
    val gradientFont = assets[BitmapFontAsset.FONT_LARGE_GRADIENT.descriptor]
    val normalFont = assets[BitmapFontAsset.FONT_DEFAULT_GRADIENT.descriptor]

    Scene2DSkin.defaultSkin = skin(atlas) { skin ->
        createLabelStyles(gradientFont, normalFont)
        label("default"){
            font = normalFont
        }
        label("gradient"){
            font = gradientFont
        }
    }
}

private fun Skin.createLabelStyles(
        bigFont: BitmapFont,
        defaultFont: BitmapFont
) {
    label(SkinLabel.LARGE.name) {
        font = bigFont
    }
    label(SkinLabel.DEFAULT.name) {
        font = defaultFont
    }
}
