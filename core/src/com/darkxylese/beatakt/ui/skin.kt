package com.darkxylese.beatakt.ui

import com.darkxylese.beatakt.assets.BitmapFontAsset
import com.darkxylese.beatakt.assets.TextureAtlasAsset
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin

fun createSkin(assets: AssetStorage){
    val atlas = assets[TextureAtlasAsset.UI.descriptor]
    val gradientFont = assets[BitmapFontAsset.FONT_LARGE_GRADIENT.descriptor]
    val normalFont = assets[BitmapFontAsset.FONT_DEFAULT_GRADIENT.descriptor]

    Scene2DSkin.defaultSkin = skin(atlas) { skin ->
        label("default"){
            font = normalFont
        }
        label("gradient"){
            font = gradientFont
        }
    }
}
