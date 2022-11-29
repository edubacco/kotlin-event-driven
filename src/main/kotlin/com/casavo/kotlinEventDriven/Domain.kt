package com.casavo.kotlinEventDriven

import kotlin.reflect.KClass

open class EventPayload

sealed class Event(
) {
    abstract val type: String
    abstract val offset: Offset

    @JvmInline
    value class Offset(val int: Int)
}

interface EventHandler<E : Event> {
    operator fun invoke(event: E): EventResult

    sealed class EventResult {
        object Success : EventResult()
        class Skip(val reason: String = "") : EventResult()
        class Failed(val reason: String) : EventResult()
    }
}

interface Cursor {
    //todo: it's a sealed
    @JvmInline
    value class Name(val name: String)
    //todo: add offset
}

/**
 * Identifies events related to a cursor
 */
sealed interface ICursorEvent

/**
 * Dispatch events to handlers for a cursor (here the compiler helps me if I do things wrong)
 */
interface ICursorDispatcher<E: ICursorEvent> {
    fun dispatch(event: E): EventHandler.EventResult
}

class CursorRunner<CE: ICursorEvent>(
    private val eventStore: EventStoreService,
    private val cursor: Cursor,
    private val cursorDispatcher: ICursorDispatcher<CE>,
    private val cursorEventInterface: KClass<CE>
) {
    fun run(): Unit {
        eventStore.getNewEvents(cursor, 1)
            .forEach(::handle)
    }

    fun handle(event: Event): Unit {
        if (cursorEventInterface.isInstance(event)) dispatch(event as CE) //todo: and dlq?
        else {} // todo: log error

        eventStore.acknowledgeEvent(event, cursor)
    }

    private fun dispatch(event: CE) {
        when (cursorDispatcher.dispatch(event)) {
            is EventHandler.EventResult.Failed -> toDlq(event) //todo: log warning
            is EventHandler.EventResult.Skip -> Unit //todo: log debug (?)
            EventHandler.EventResult.Success -> Unit
        }
    }

    private fun toDlq(event: CE): Unit = Unit //todo: implement DLQ
}

interface EventStoreService {
    fun acknowledgeEvent(event: Event, cursor: Cursor)
    fun getNewEvents(
        cursor: Cursor,
        maxReturnedEvents: Int
    ): List<Event>
}

//todo: non ti serve un DLQservice per cursore, te ne basta uno solo (che prende o evento e cursore o evento col tipo
// del cursore
interface DLQService<C : Cursor> {
    fun hasToBeQueued(event: Event): Boolean
    fun enqueue(event: Event): Result<Unit>
    fun dequeue(event: Event): Result<Unit>

    private fun rawEnqueue(cursorName: Cursor.Name) {

    }
}
