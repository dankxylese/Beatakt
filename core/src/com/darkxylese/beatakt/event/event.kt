package com.darkxylese.beatakt.event

import ktx.collections.GdxSet
import java.util.*

enum class GameEventType{
    NONE,
    PLAYER_DEATH,
    TOUCH1,
    TOUCH2,
    TOUCH3,
    TOUCH4
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
    var bestStreak = 0

    override fun toString() = "GameEventPlayerDeath(Hits= $hits)"
}

interface GameEventListener {
    fun onEvent(type: GameEventType, data:GameEvent? = null)
}

//Decouples components of the system
class GameEventManager{
    private val listeners = EnumMap<GameEventType, GdxSet<GameEventListener>>(GameEventType::class.java)


    fun addListener(type: GameEventType, listener: GameEventListener){
        //check if we have listeners of the same type
        var eventListeners = listeners[type]

        //if not, we add it to the map. Make sure event listeners are a valid set
        if (eventListeners == null){
            eventListeners = GdxSet()
            listeners[type] = eventListeners
        }

        eventListeners.add(listener)
    }

    fun removeListener(type: GameEventType, listener: GameEventListener){
        listeners[type]?.remove(listener)
    }

    fun removeListener(listener: GameEventListener){
        listeners.values.forEach { it.remove(listener) }
    }

    //What event gets dispatched + data
    //Any class that has access to this can dispatch an event and notify all the listeners of the certain event.
    fun dispatchEvent(type:GameEventType, data:GameEvent?=null){
        listeners[type]?.forEach { it.onEvent(type,data) }
    }
}