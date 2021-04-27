package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.assets.SoundAsset
import com.darkxylese.beatakt.assets.TextureAsset
import com.darkxylese.beatakt.assets.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger

private val log = logger<LoadingScreen>()

class LoadingScreen(game: Beatakt) : BeataktScreen(game) {
    override fun show() {
        val old = System.currentTimeMillis()
        log.debug { "Loading Screen is Shown" }

        //queue asset loading
        val assetRefs = gdxArrayOf( //asset manager that takes care of asset handling
                TextureAsset.values().map { assets.loadAsync(it.descriptor)},
                TextureAtlasAsset.values().map {assets.loadAsync(it.descriptor)},
                SoundAsset.values().map {assets.loadAsync(it.descriptor)}
        ).flatten() //flatten the entire list. Before we had an array, so we make it into a list



        // once loading finished change screens
        KtxAsync.launch { //non blocking
            assetRefs.joinAll()
            //loading is done
            log.debug { "Time for loading assets : ${System.currentTimeMillis() - old} ms" }
            assetsLoaded()
        }


        //setup ui.. loading bar etc. while graphics are loading in parallel
        //addScreen(GameScreen(this))
    }

    private fun assetsLoaded(){
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose() //remove loading screen
    }


    override fun render(delta: Float) {
        if(Gdx.input.justTouched()){
            game.setScreen<GameScreen>()
        }
    }



}