package com.darkxylese.beatakt.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.assets.I18NBundleAsset
import com.darkxylese.beatakt.assets.MusicAsset
import com.darkxylese.beatakt.ecs.component.*
import com.darkxylese.beatakt.ecs.system.CollisionScoreSystem
import com.darkxylese.beatakt.ecs.system.ScoreSystem
import com.darkxylese.beatakt.ecs.system.SpawnSystem
import com.darkxylese.beatakt.event.GameEvent
import com.darkxylese.beatakt.event.GameEventListener
import com.darkxylese.beatakt.event.GameEventPlayer
import com.darkxylese.beatakt.event.GameEventType
import com.darkxylese.beatakt.ui.GameUi
import ktx.actors.plusAssign
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.getSystem
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import java.lang.Float.min


private val log = logger<GameScreen>()
private const val MAX_DELTA_TIME = 1/30f
const val HITBOX_HEIGHT = 2f
const val INPUT_TIMEOUT = 1.2f
const val SPAWN_SPEED = 10f
const val BANDS_FILTER_STRENGHT = 0.2f //0.29
const val BAND1_FILTER_POST_LIMIT = 20f //30
const val BANDS23_FILTER_POST_LIMIT = 7f //10
const val MISSES_ALLOWED = 8
const val SCAN_RANGE = 25

class GameScreen(
        game: Beatakt,
        private val engine: Engine = game.engine,
) : GameEventListener, BeataktScreen(game) {
    private val ui = GameUi(assets[I18NBundleAsset.DEFAULT.descriptor])


    private val score = engine.entity {
        with<ScoreComponent>{
            beatMapLoc = Gdx.files.external("Beatakt/WestCoastZHU.bm")
            beatMapName = "WestCoastZHU"
            beatSongLoc = Gdx.files.external("Beatakt/Songs/WestCoastZHU.mp3")
            length = 260f
        }
    }
    /*
    private val score = engine.entity {
        with<ScoreComponent>{
            beatMapLoc = Gdx.files.external("Beatakt/OdeToCharles.bm")
            beatMapName = "OdeToCharles"
            beatSongLoc = Gdx.files.external("Beatakt/Songs/OdeToCharles.mp3")
            length = 252f
        }
    }*/

    /*
    private val score = engine.entity {
        with<ScoreComponent>{
            beatMapLoc = Gdx.files.external("Beatakt/Exodus.bm")
            beatMapName = "Exodus"
            beatSongLoc = Gdx.files.external("Beatakt/Songs/Exodus.mp3")
            length = 176f
        }
    }*/


    override fun show() {
        gameEventManager.addListener(GameEventType.PLAYER_DEATH, this)
        gameEventManager.addListener(GameEventType.ENDGAME, this)
        gameEventManager.addListener(GameEventType.TOUCH, this)

        log.debug { "Game BeataktScreen is Shown" }

        audioService.play(MusicAsset.WestCoast)
        //audioService.play(MusicAsset.Exodus)
        //audioService.play(MusicAsset.OdeToCharles)

        val playerHitbox = spawnPlayer()
        createGameElements()
        var lenght = 0f
        var beatMapLocation: FileHandle? = null

        score[ScoreComponent.mapper].let { score ->
            if (score != null) {
                beatMapLocation = score.beatMapLoc
                lenght = score.length
                log.debug { "${preferences[score.beatMapName, 0f]}" }
            }
        }


        var resultBand4: MutableList<Float> = processFFT(beatMapLocation)

        engine.apply {
            addSystem(SpawnSystem(resultBand4, gameEventManager, playerHitbox, 1/(resultBand4.size / lenght), ((16-3.125)/SPAWN_SPEED).toInt()))
                                                                                        //16 units is the in world height, -2 is the placement of the hitbox (the =) and -1.125 is half of the hitbox )
            addSystem(CollisionScoreSystem(playerHitbox, audioService, gameEventManager))
            addSystem(ScoreSystem(game.gameEventManager))
        }

        setupUI()

    }

    override fun hide() {
        super.hide()
        gameEventManager.removeListener(this)
    }

    fun spawnPlayer(): Entity {
        val playerHitbox = engine.entity {
            with<TransformComponent>{
                setInitPos(0f, 2f, 2f)
            }
            with<PlayerComponent>()
            with<TransformCollisionComponent>{
                setInitBox(0f, HITBOX_HEIGHT, (9f/(1080/270))*0.8f, (16f/(1920/270))*1.8f)
            }
            with<ScoreComponent>()
            with<GraphicComponent>{id=SpriteIDs.PLAYER}
        }
        return playerHitbox
    }

    fun createGameElements(){
        //Hitbox 1
        engine.entity {
            with<TransformComponent>{
                setInitPos(0f, 2f, 1f)
            }
            with<GraphicComponent>{id=SpriteIDs.HITBOX}
        }
        //Hitbox 2
        engine.entity {
            with<TransformComponent>{
                setInitPos(2.25f, 2f, 1f)
            }
            with<GraphicComponent>{id=SpriteIDs.HITBOX}
        }
        //Hitbox 3
        engine.entity {
            with<TransformComponent>{
                setInitPos(4.5f, 2f, 1f)
            }
            with<GraphicComponent>{id=SpriteIDs.HITBOX}
        }
        //Hitbox 4
        engine.entity {
            with<TransformComponent>{
                setInitPos(6.75f, 2f, 1f)
            }
            with<GraphicComponent>{id=SpriteIDs.HITBOX}
        }

    }

    private fun setupUI() {
        ui.run {
            // reset to initial values
            updateScore(0)
            updateLife(
                    MISSES_ALLOWED,
                    MISSES_ALLOWED
            )
            updateAccu("")
        }
        stage += ui
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
        audioService.update()

        val deltaTime = kotlin.math.min(delta, MAX_DELTA_TIME)
        // render UI
        stage.run {
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }

    override fun onEvent(type: GameEventType, data: GameEvent?) {
        if (type == GameEventType.PLAYER_DEATH){
            log.debug { "PLAYER DIED" }
            audioService.stop()
            engine.getSystem<SpawnSystem>().setProcessing(false)
            val eventData = data as GameEventPlayer
            score[ScoreComponent.mapper].let { score ->
                preferences.flush {
                    this[score!!.beatMapName] = (eventData.score)
                }
            }
        }

        if (type == GameEventType.TOUCH){
            val eventData = data as GameEventPlayer
            ui.run {
                updateScore(eventData.score)
                log.debug {eventData.score.toString()}
                log.debug { "UPDATE SCORE" }
                updateLife(MISSES_ALLOWED - eventData.missStreak, MISSES_ALLOWED)
                updateAccu(eventData.accu)
                updateStreak("X${eventData.streak}")
            }
        }

        if (type == GameEventType.ENDGAME){
            log.debug { "PLAYER FINISHED" }
            audioService.stop()
            engine.getSystem<SpawnSystem>().setProcessing(false)
            val eventData = data as GameEventPlayer
            score[ScoreComponent.mapper].let { score ->
                preferences.flush {
                    this[score!!.beatMapName] = (eventData.score)
                }
            }
        }

    }

    private fun processFFT(beatMapLocation: FileHandle?): MutableList<Float> {
        var textD = ""

        if (beatMapLocation != null) {
            textD = beatMapLocation!!.readString()
        }
        val bands = textD.split(";").toTypedArray() //3 bands of analysed frequencies
        var band1String = bands[0].split(",").toTypedArray()
        val band1 = band1String.map { it.toFloat() }
        var band2String = bands[1].split(",").toTypedArray()
        val band2 = band2String.map { it.toFloat() }
        var band3String = bands[2].split(",").toTypedArray()
        val band3 = band3String.map { it.toFloat() }

        var resultBand1: MutableList<Float> = ArrayList()
        var resultBand2: MutableList<Float> = ArrayList()
        var resultBand3: MutableList<Float> = ArrayList()
        var result: MutableList<Float> = ArrayList()
        var c1 = 0 //counter1
        var largest = 0.0f
        var largestPos = 0
        var c2 = 0
        var helper = false
        var c2helper = false
        var minPower = 0f


        while (c1 < band1.size) { //fill result with empty
            resultBand1.add(0f)
            resultBand2.add(0f)
            resultBand3.add(0f)
            result.add(0f)
            c1++
        }

        //pass 1
        c1 = 0

        var c3 = 0
        var c3total = 0f

        while (c1 < band1.size) {
            if (band1[c1] > BAND1_FILTER_POST_LIMIT) {
                c3++
                c3total += band1[c1]
            }
            c1++
        }

        minPower = (c3total / c3) * BANDS_FILTER_STRENGHT //filter output

        c1 = 0
        largest = 0.0f
        largestPos = 0

        while (c1 < band1.size) {
            if (band1[c1] > 0.0f && band1[c1 + 1] == 0.0f) {
                if (band1[c1] > minPower) {
                    resultBand1[c1] = band1[c1]
                }
                helper = true //help iterate while loop without affecting next if check
            }

            if (band1[c1] > 0.0f && band1[c1 + 1] > 0.0f && !c2helper) {
                while (band1[c1 + c2] > 0.0f) {
                    if (band1[c1 + c2] > largest) {
                        largest = band1[c1 + c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos

            }

            if (band1[c1] > 0.0f && band1[c1 + 1] > 0.0f && c2helper) {
                if (band1[c1] > minPower) {
                    resultBand1[c1] = band1[c1]
                }
                c1 += c2 - largestPos //help iterate while loop without affecting next if check
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
            if (helper) {
                c1++
                helper = false
            }
            if (band1[c1] == 0.0f) {
                c1++
            }
        }


        //pass 2

        c1 = 0
        c2 = 0
        helper = false
        c2helper = false
        c3 = 0
        c3total = 0f


        while (c1 < band2.size) {
            if (band2[c1] > BANDS23_FILTER_POST_LIMIT) {
                c3++
                c3total += band2[c1]
            }
            c1++
        }

        minPower = (c3total / c3) * BANDS_FILTER_STRENGHT //filter output

        c1 = 0
        largest = 0.0f
        largestPos = 0

        while (c1 < band2.size) {
            if (band2[c1] > 0.0f && band2[c1 + 1] == 0.0f) {
                if (band2[c1] > minPower) {
                    resultBand1[c1] = band2[c1]
                }
                helper = true //help iterate while loop without affecting next if check
            }

            if (band2[c1] > 0.0f && band2[c1 + 1] > 0.0f && !c2helper) {
                while (band2[c1 + c2] > 0.0f) {
                    if (band2[c1 + c2] > largest) {
                        largest = band2[c1 + c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos

            }

            if (band2[c1] > 0.0f && band2[c1 + 1] > 0.0f && c2helper) {
                if (band2[c1] > minPower) {
                    resultBand1[c1] = band2[c1]
                }
                c1 += c2 - largestPos //help iterate while loop without affecting next if check
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
            if (helper) {
                c1++
                helper = false
            }
            if (band2[c1] == 0.0f) {
                c1++
            }
        }

        //pass 3

        c1 = 0
        c2 = 0
        helper = false
        c2helper = false
        c3 = 0
        c3total = 0f


        while (c1 < band3.size) {
            if (band3[c1] > BANDS23_FILTER_POST_LIMIT) {
                c3++
                c3total += band3[c1]
            }
            c1++
        }

        minPower = (c3total / c3) * BANDS_FILTER_STRENGHT //filter output

        c1 = 0
        largest = 0.0f
        largestPos = 0

        while (c1 < band3.size) {
            if (band3[c1] > 0.0f && band3[c1 + 1] == 0.0f) {
                if (band3[c1] > minPower) {
                    resultBand1[c1] = band3[c1]
                }
                helper = true //help iterate while loop without affecting next if check
            }

            if (band3[c1] > 0.0f && band3[c1 + 1] > 0.0f && !c2helper) {
                while (band3[c1 + c2] > 0.0f) {
                    if (band3[c1 + c2] > largest) {
                        largest = band3[c1 + c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos

            }

            if (band3[c1] > 0.0f && band3[c1 + 1] > 0.0f && c2helper) {
                if (band3[c1] > minPower) {
                    resultBand1[c1] = band3[c1]
                }
                c1 += c2 - largestPos //move pointer to end of large scan range
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
            if (helper) {
                c1++
                helper = false
            }
            if (band3[c1] == 0.0f) {
                c1++
            }
        }

        helper = false
        c2helper = false

        c1 = 0
        c2 = 0
        largest = 0.0f
        largestPos = 0

        //pass 4
        while (c1 < resultBand1.size) {

            if (c1 > (resultBand1.size - SCAN_RANGE+1)) { //if we have less that 10 samples left to analyse, break to avoid out of bound exc
                break
            }
            if (!c2helper) {
                while (c2 <= SCAN_RANGE) { //25 is our scan range - this can be increased or decreased to change difficulty - bake into settings file
                    if (resultBand1[c1 + c2] > largest) { //find largest (strongest beat) in the range
                        largest = resultBand1[c1 + c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos
            }


            if (c2helper) {
                result[c1] = resultBand1[c1]
                c1 += c2 - largestPos //move pointer to end of large scan range
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
        }
        return result
    }

}