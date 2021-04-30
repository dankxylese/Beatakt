package com.darkxylese.beatakt.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.darkxylese.beatakt.Beatakt

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration().apply {
            setWindowedMode(480, 854)
            setTitle("Beatakt")
        }
        Lwjgl3Application(Beatakt(), config).logLevel = Application.LOG_DEBUG
    }
}

