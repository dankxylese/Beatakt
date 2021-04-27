package com.darkxylese.beatakt.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import ktx.assets.getAsset
import ktx.assets.load
import com.badlogic.gdx.graphics.g2d.TextureAtlas

enum class TextureAsset(
        fileName: String,
        directory: String = "images",
        val descriptor: AssetDescriptor<Texture> = AssetDescriptor("$directory/$fileName", Texture::class.java)
){
    BACKGROUND1("background1.png"),
    BACKGROUND2("background2.png")
}

enum class TextureAtlasAsset(
        fileName: String,
        directory: String = "images",
        val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
){
    GAME_GRAPHICS("GameElements.atlas")
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
    TESTGAMEMUSIC("WestCoastZHU.mp3")
}