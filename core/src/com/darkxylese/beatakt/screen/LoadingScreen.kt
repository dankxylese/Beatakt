package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.assets.SoundAsset
import com.darkxylese.beatakt.assets.TextureAsset
import com.darkxylese.beatakt.assets.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.scene2d.actors
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.stack
import ktx.scene2d.table

private val log = logger<LoadingScreen>()

class LoadingScreen(game: Beatakt) : BeataktScreen(game) {
    private lateinit var progressBar: Image
    private lateinit var touchToBeginLabel: Label


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


        setupUI()
    }

    override fun hide() {
        stage.clear()
    }

    private fun assetsLoaded(){
        game.addScreen(GameScreen(game))
        touchToBeginLabel += forever(sequence(fadeIn(0.5f) + fadeOut(0.5f)))
    }


    override fun render(delta: Float) {
        if(assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<GameScreen>()){
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose() //remove loading screen
        }

        progressBar.scaleX = assets.progress.percent
        stage.run{
            viewport.apply()
            act()
            draw()
        }

    }

    private fun setupUI(){
        stage.actors{
            table {
                defaults().fillX().expandX()

                label("Beatact","gradient"){
                    setWrap(true)
                    setAlignment(Align.center)
                }
                row()

                touchToBeginLabel = label("Touch to Begin","default"){
                    setWrap(true)
                    setAlignment(Align.center)
                    color.a = 0f
                }
                row()

                stack { cell ->
                    progressBar = image("life_bar").apply{
                        scaleX = 0f
                    }
                    label("Loading...", "default"){
                        setAlignment(Align.center)
                    }
                    cell.padLeft(5f).padRight(5f)
                }

                setFillParent(true)
                pack()
            }
        }
    }

}