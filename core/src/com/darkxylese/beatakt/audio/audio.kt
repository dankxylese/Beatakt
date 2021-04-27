package com.darkxylese.beatakt.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Pool
import com.darkxylese.beatakt.assets.MusicAsset
import com.darkxylese.beatakt.assets.SoundAsset
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import java.lang.Float.max
import java.util.*


private val log = logger<AudioService>()
private const val MAX_SOUND_INTANCES = 16 //max sound effects possible on android

interface AudioService {


    fun play(soundAsset: SoundAsset, volume: Float = 1f) //soundAsset - small piece of audio (small memory use)
    fun play(musicAsset: MusicAsset, volume: Float = 1f, loop: Boolean = true) //musicAsset - longer music (heavier memory and more expensive to keep in memory (keep 1 or 2 for fade inbetween))
    fun pause()
    fun resume()
    fun stop(clearSounds: Boolean = true)
    fun update() //goes through the queue of sounds as to not increase the volume if a sound effect gets called many times in a frame
}

private class SoundRequest : Pool.Poolable{
    lateinit var soundAsset: SoundAsset
    var volume = 1f

    override fun reset() {
        volume = 1f
    }
}
private class SoundRequestPool : Pool<SoundRequest>() {
    override fun newObject() = SoundRequest()
}

class DefaultAudioService(private val assets: AssetStorage) :AudioService{
    private val soundCache = EnumMap<SoundAsset, Sound>(SoundAsset::class.java) //O(1) complexity for improved performance of access
    private val soundRequestPool = SoundRequestPool()
    private val soundRequests = EnumMap<SoundAsset, SoundRequest>(SoundAsset::class.java)
    private var currentMusic: Music? = null //reference to old music if we play a new track
    private var currentMusicAsset: MusicAsset? = null //does not matter what we init it with



    override fun play(soundAsset: SoundAsset, volume: Float) {
        when{
            soundAsset in soundRequests ->{ //check if in queue
                //same sound request in one frame many times
                //play sound once with highest volume of both requests

                soundRequests[soundAsset]?. let{ request ->
                    request.volume = max(request.volume, volume)
                }
            }
            soundRequests.size >= MAX_SOUND_INTANCES ->{ //if too many requests, stop
                log.debug { "Max sound request reached" }
                return
            }
            else -> { //otherwise, first request
                if (soundAsset.descriptor !in assets){ //is it loaded properly
                    log.error { "Trying to play a sound thats not loaded" }
                    return
                } else if (soundAsset !in soundCache){ //is it stored in cache
                    soundCache[soundAsset] = assets[soundAsset.descriptor] //asset intance out of asset storage and add to enum map
                }

                //add sound request to queue
                soundRequests[soundAsset] = soundRequestPool.obtain().apply {
                    this.soundAsset = soundAsset
                    this.volume = volume
                }
            }
        }
    }

    override fun play(musicAsset: MusicAsset, volume: Float, loop: Boolean) {
        val musicDeferred = assets.loadAsync(musicAsset.descriptor)
        KtxAsync.launch {
            musicDeferred.join() //wait for it to load
            if(assets.isLoaded(musicAsset.descriptor)){ //if we loaded it once, unloaded it, and when its done in join
                                                        // but its not loaded completely (if switching between music quickly)

                currentMusic?.stop()
                val currentAsset = currentMusicAsset
                if (currentAsset != null){
                    assets.unload(currentAsset.descriptor)
                }
                currentMusicAsset = musicAsset
                currentMusic = assets[musicAsset.descriptor].apply { //set current song
                    this.volume = volume
                    this.isLooping = loop
                    play()
                }
            }
        }
    }

    override fun pause() {
         currentMusic?.pause()
    }

    override fun resume() {
        currentMusic?.play() //resume from previous position
    }

    override fun stop(clearSounds: Boolean) {
        currentMusic?.stop()
        if (clearSounds){
            soundRequests.clear()
        }
    }

    override fun update() {
        if(!soundRequests.isEmpty()){ //do we have requests
            soundRequests.values.forEach { request -> //iterate over all of them
                soundCache[request.soundAsset]?.play(request.volume) //check sound cache, extract and play
                soundRequestPool.free(request) //once processed, give up the request to the pool for reuse later
            }
            soundRequests.clear() //clear requests for next frame
        }
    }

}