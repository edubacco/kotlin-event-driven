package com.casavo.kotlinEventDriven

open class EventPayload

sealed class Event<EP : EventPayload>(
    open val type: String,
    open val offset: Offset,
    open val payload: EP
) {
    @JvmInline
    value class Offset(val int: Int)
}

interface EventHandler<E : Event<*>> {
    operator fun invoke(event: E): EventResult

    sealed class EventResult {
        object Success : EventResult()
        class Skip(val reason: String = "") : EventResult()
        class Failed(val reason: String) : EventResult()
    }
}

interface Cursor {
    @JvmInline
    value class Name(val name: String)
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

interface EventStoreService {
    fun acknowledgeEvent(event: Event<*>, cursor: Cursor)
    fun getNewEvents(
        cursor: Cursor,
        maxReturnedEvents: Int
    ): List<Event<*>>
}

//todo: non ti serve un DLQservice per cursore, te ne basta uno solo (che prende o evento e cursore o evento col tipo
// del cursore
interface DLQService<C : Cursor> {
    fun hasToBeQueued(event: Event<*>): Boolean
    fun enqueue(event: Event<*>): Result<Unit>
    fun dequeue(event: Event<*>): Result<Unit>

    private fun rawEnqueue(cursorName: Cursor.Name) {

    }
}
