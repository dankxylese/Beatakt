package com.darkxylese.beatakt.screen

import com.badlogic.gdx.Gdx
import com.darkxylese.beatakt.Beatakt
import com.darkxylese.beatakt.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.Float.min
import com.darkxylese.beatakt.fft


private val log = logger<GameScreen>()
private const val MAX_DELTA_TIME = 1/30f

class GameScreen(game: Beatakt) : BeataktScreen(game) {
    //private val texture = Texture(Gdx.files.internal("images/boxa.png"))

    private val playerHitbox = engine.entity {
        with<TransformComponent>{
            position.set(0f, 2f, 2f)
        }
        with<PlayerComponent>()
        with<GraphicComponent>{id=SpriteIDs.PLAYER}
    }

    private val hit = engine.entity {
        with<TransformComponent>{

            position.set(2.25f, 10f, 0f)
        }
        with<GraphicComponent>{id=SpriteIDs.HIT}
        with<HitMoveComponent> { speed = 2f}
    }
    private val hitboxA = engine.entity {
        with<TransformComponent>{
            position.set(0f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxB = engine.entity {
        with<TransformComponent>{
            position.set(2.25f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxC = engine.entity {
        with<TransformComponent>{
            position.set(4.5f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }
    private val hitboxD = engine.entity {
        with<TransformComponent>{
            position.set(6.75f, 2f, 1f)
        }
        with<GraphicComponent>{id=SpriteIDs.HITBOX}
    }


    override fun show() {
        log.debug { "Game BeataktScreen is Shown" }

        //Test Output
        var outColF: BufferedWriter? = null
        try {
            if (!Gdx.files.local("test").exists()) Gdx.files.local("test").mkdirs()
            if (Gdx.files.local("test/test.bm").exists()) Gdx.files.local("test/test.bm").delete()
            outColF = BufferedWriter(OutputStreamWriter(Gdx.files.local("test/test.bm").write(true)))
            outColF.write("test Beatmap")
        } catch (e: Throwable) {
        } finally {
            try {
                outColF?.close()
            } catch (e: IOException) {
            }
        }
        val test = Gdx.files.externalStoragePath
        val test1 = Gdx.files.absolute(".").list().size               //Returned 63
        val test2 = Gdx.files.absolute("./storage").list()        //Returned 4
        val test3 = Gdx.files.absolute("./storage/self/primary").list()  //Returned 0
        val files = Gdx.files.external("/Music").list()
        val song = Gdx.files.external("/Music/Midnight.mp3")
        ProcessFFT
        for (file in files) {
            //log.debug { file.path().toString() }
            log.debug { file.path().toString() }
        }



        /*
        val selection: String = MediaStore.Audio.Media.IS_MUSIC + " != 0"


        val projection = arrayOf<String>(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        )

        cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)

        val songs: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            songs.add(cursor.getString(0).toString() + "||" + cursor.getString(1) + "||" + cursor.getString(2) + "||" + cursor.getString(3) + "||" + cursor.getString(4) + "||" + cursor.getString(5))
        } */


    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }

}