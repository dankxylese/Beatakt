package com.darkxylese.beatakt.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.I18NBundle
import org.w3c.dom.Text

enum class TextureAsset(
        fileName: String,
        directory: String = "images",
        val descriptor: AssetDescriptor<Texture> = AssetDescriptor("$directory/$fileName", Texture::class.java)
){
    BACKGROUND1("background1.png"),
    BACKGROUND2("background2.png")
}

enum class TextureAtlasAsset(
        val isSkinAtlas: Boolean,
        fileName: String,
        directory: String = "images",
        val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
){
    GAME_GRAPHICS(false, "GameElements.atlas"),
    UI(true, "ui.atlas")
}

enum class SoundAsset(
        fileName: String,
        directory: String = "sounds",
        val descriptor: AssetDescriptor<Sound> = AssetDescriptor("$directory/$fileName", Sound::class.java)
) {
    HIT("hit.wav"),
    FINISH("finish.wav"),
    FAIL("fail.wav")
}

enum class MusicAsset(
        fileName: String,
        directory: String = "music",
        val descriptor: AssetDescriptor<Music> = AssetDescriptor("$directory/$fileName", Music::class.java)
) {
    STARTMUSIC("menu.mp3"),
    WestCoast("WestCoastZHU.mp3"),
    Exodus("Exodus.mp3"),
    Poobitsiai("poobitsiai_meni.mp3"),
    OdeToCharles("OdeToCharles.mp3")
}

enum class BeatMapAsset(
        fileName: String,
        directory: String = "testMaps",
        val descriptor: AssetDescriptor<Music> = AssetDescriptor("$directory/$fileName", Music::class.java)
) {
    WestCoast("WestCoastZHU.bm"),
    Exodus("Exodus.bm"),
    Poobitsiai("poobitsiai_meni.bm"),
    OdeToCharles("OdeToCharles.bm")
}

enum class BitmapFontAsset(
        fileName: String,
        directory: String = "images",
        val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor(
                "$directory/$fileName",
                BitmapFont::class.java,
                BitmapFontLoader.BitmapFontParameter().apply {
                    atlasName = TextureAtlasAsset.UI.descriptor.fileName
                }
        )
){
    FONT_LARGE_GRADIENT("font11_gradient.fnt"),
    FONT_DEFAULT_GRADIENT("font8.fnt")
}

enum class I18NBundleAsset(
        fileName: String,
        directory: String = "i18n",
        val descriptor: AssetDescriptor<I18NBundle> = AssetDescriptor("$directory/$fileName", I18NBundle::class.java)
) {
    DEFAULT("i18n")
}
