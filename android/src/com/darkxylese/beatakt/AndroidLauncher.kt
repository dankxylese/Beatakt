package com.darkxylese.beatakt

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.darkxylese.beatakt.ui.Game

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration().apply {
            //hideStatusBar = true
            //useImmersiveMode = true
        }
        initialize(Game(), config)
    }
}