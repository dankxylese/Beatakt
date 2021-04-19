package com.darkxylese.beatakt.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music
import ktx.assets.getAsset
import ktx.assets.load
import com.badlogic.gdx.graphics.g2d.TextureAtlas

// sounds
enum class SoundAssets(val path: String) {
    Hit("sounds/hit.wav")
}

inline fun AssetManager.load(asset: SoundAssets) = load<Sound>(asset.path)
inline operator fun AssetManager.get(asset: SoundAssets) = getAsset<Sound>(asset.path)

// music
enum class MusicAssets(val path: String) {
    Song("music/testaudio.mp3")
}

inline fun AssetManager.load(asset: MusicAssets) = load<Music>(asset.path)
inline operator fun AssetManager.get(asset: MusicAssets) = getAsset<Music>(asset.path)

// texture atlas
enum class TextureAtlasAsset (
    val isSkinAtlas: Boolean,
    fileName: String,
    directory: String = "images",
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
){
    GRAPHICS(false, "game.atlas")
    //UI(true, "ui.atlas", "ui")
}
