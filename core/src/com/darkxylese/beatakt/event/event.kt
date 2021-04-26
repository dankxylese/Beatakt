package com.darkxylese.beatakt.event

enum class GameEventType{
    PLAYER_DEATH
}

interface GameEvent



//Object because there shouldn't be many events in a frame. To create only 1 we make an object
object GameEventPlayerDeath : GameEvent {
    var hits = 0 //total
    var score = 0

    var s0count = 0
    var s50count = 0
    var s100count = 0
    var s300count = 0
    var streak = 0

    override fun toString() = "GameEventPlayerDeath(Hits= $hits)"

}