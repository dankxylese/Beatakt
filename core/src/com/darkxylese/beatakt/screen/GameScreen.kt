package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.math.Vector3
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.ecs.component.*
import com.darkxylese.beatakt.ecs.system.CollisionScoreSystem
import com.darkxylese.beatakt.ecs.system.SpawnSystem
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import java.lang.Float.min
import java.util.*
import kotlin.collections.ArrayList


private val log = logger<GameScreen>()
private const val MAX_DELTA_TIME = 1/30f

class GameScreen(game: Beatakt) : BeataktScreen(game) {

    private val score = engine.entity {
        with<ScoreComponent>{
            beatMapLoc = Gdx.files.external("Beatakt/DiscoFries.bm")
            beatMapName = "AndIMayCry"
            beatSongLoc = Gdx.files.external("/Music/DiscoFries.mp3")
        }
    }

    private val playerHitbox = engine.entity {
        with<TransformComponent>{
            setInitPos(0f, 2f, 2f)
            //bounds.y = 2f
        }
        with<PlayerComponent>()
        with<TransformCollisionComponent>{
            setInitBox(0f, 3f, (9f/(1080/270))*0.7f, (16f/(1920/270))*1.3f)
        }
        with<ScoreComponent>()
        with<GraphicComponent>{id=SpriteIDs.PLAYER}
    }

    private val hitboxA = engine.entity {
        with<TransformComponent>{
            setInitPos(0f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxB = engine.entity {
        with<TransformComponent>{
            setInitPos(2.25f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxC = engine.entity {
        with<TransformComponent>{
            setInitPos(4.5f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxD = engine.entity {
        with<TransformComponent>{
            setInitPos(6.75f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }


    override fun show() {
        log.debug { "Game BeataktScreen is Shown" }

        var beatMapLocation: FileHandle? = null
        var textD = ""


        score[ScoreComponent.mapper].let { score ->
            if (score != null) {
                beatMapLocation = score.beatMapLoc
            }
        }

        if (beatMapLocation != null){
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
        var resultBand4: MutableList<Float> = ArrayList()
        var c1 = 0 //counter1
        var largest = 0.0f
        var largestPos = 0
        var c2 = 0
        var helper = false
        var c2helper = false
        var minPower = 0f


        while(c1 < band1.size){ //fill result with empty
            resultBand1.add(0f)
            resultBand2.add(0f)
            resultBand3.add(0f)
            resultBand4.add(0f)
            c1++
        }

        //pass 1
        c1 = 0

        var c3 = 0
        var c3total = 0f

        while(c1 < band1.size){
            if (band1[c1] > 30f){
                c3++
                c3total += band1[c1]
            }
            c1++
        }

        minPower = (c3total / c3)*0.29f //filter output

        c1 = 0
        largest = 0.0f
        largestPos = 0

        while(c1 < band1.size){
            if (band1[c1] > 0.0f && band1[c1+1] == 0.0f){
                if(band1[c1] > minPower){resultBand1[c1] = band1[c1]}
                helper = true //help iterate while loop without affecting next if check
            }

            if (band1[c1] > 0.0f && band1[c1+1] > 0.0f && !c2helper) {
                while (band1[c1+c2] > 0.0f){
                    if (band1[c1+c2] > largest){
                        largest = band1[c1+c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos

            }

            if (band1[c1] > 0.0f && band1[c1+1] > 0.0f && c2helper) {
                if(band1[c1] > minPower){resultBand1[c1] = band1[c1]}
                c1 += c2 - largestPos //help iterate while loop without affecting next if check
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
            if (helper){
                c1++
                helper = false
            }
            if (band1[c1] == 0.0f){
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


        while(c1 < band2.size){
            if (band2[c1] > 10f){
                c3++
                c3total += band2[c1]
            }
            c1++
        }

        minPower = (c3total / c3)*0.29f //filter output

        c1 = 0
        largest = 0.0f
        largestPos = 0

        while(c1 < band2.size){
            if (band2[c1] > 0.0f && band2[c1+1] == 0.0f){
                if(band2[c1] > minPower){resultBand1[c1] = band2[c1]}
                helper = true //help iterate while loop without affecting next if check
            }

            if (band2[c1] > 0.0f && band2[c1+1] > 0.0f && !c2helper) {
                while (band2[c1+c2] > 0.0f){
                    if (band2[c1+c2] > largest){
                        largest = band2[c1+c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos

            }

            if (band2[c1] > 0.0f && band2[c1+1] > 0.0f && c2helper) {
                if(band2[c1] > minPower){resultBand1[c1] = band2[c1]}
                c1 += c2 - largestPos //help iterate while loop without affecting next if check
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
            if (helper){
                c1++
                helper = false
            }
            if (band2[c1] == 0.0f){
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


        while(c1 < band3.size){
            if (band3[c1] > 10f){
                c3++
                c3total += band3[c1]
            }
            c1++
        }

        minPower = (c3total / c3)*0.29f //filter output

        c1 = 0
        largest = 0.0f
        largestPos = 0

        while(c1 < band3.size){
            if (band3[c1] > 0.0f && band3[c1+1] == 0.0f){
                if(band3[c1] > minPower){resultBand1[c1] = band3[c1]}
                helper = true //help iterate while loop without affecting next if check
            }

            if (band3[c1] > 0.0f && band3[c1+1] > 0.0f && !c2helper) {
                while (band3[c1+c2] > 0.0f){
                    if (band3[c1+c2] > largest){
                        largest = band3[c1+c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos

            }

            if (band3[c1] > 0.0f && band3[c1+1] > 0.0f && c2helper) {
                if(band3[c1] > minPower){resultBand1[c1] = band3[c1]}
                c1 += c2 - largestPos //move pointer to end of large scan range
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
            if (helper){
                c1++
                helper = false
            }
            if (band3[c1] == 0.0f){
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
        while(c1 < resultBand1.size){

            if (c1 > (resultBand1.size - 26)){ //if we have less that 10 samples left to analyse, break to avoid out of bound exc
                break
            }
            if (!c2helper){
                while (c2 <= 25){ //25 is our scan range - this can be increased or decreased to change difficulty - bake into settings file
                    if (resultBand1[c1+c2] > largest){ //find largest (strongest beat) in the range
                        largest = resultBand1[c1+c2]
                        largestPos = c2
                    }
                    c2++
                }
                c2helper = true
                c1 += largestPos //move pointer to largest pos
            }


            if (c2helper) {
                resultBand4[c1] = resultBand1[c1]
                c1 += c2 - largestPos //move pointer to end of large scan range
                c2helper = false
                c2 = 0
                largest = 0f
                largestPos = 0
            }
        }

        //log.debug { result.toString() }
        //log.debug { bands.toString() }

        engine.apply {
            addSystem(SpawnSystem(resultBand4, playerHitbox))
            addSystem(CollisionScoreSystem(playerHitbox))
        }
        //val text: String = handle.readString()


    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }

}