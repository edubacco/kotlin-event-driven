package com.casavo.kotlinEventDriven

class Event<T : EventType>(
    val type: T,
    val offset: Offset,
    val payload: EventPayload<T>
) {
    @JvmInline
    value class Offset(val int: Int)
}

sealed class EventType(
    open val name: String
)

class EventPayload<T : EventType>

interface EventHandler<T : EventType> {
    fun invoke(event: Event<T>): Result<*>
}

interface Cursor {
    @JvmInline
    value class Name(val name: String)
}

// defines events related to a single cursor
sealed interface ICursorEvent {
    val event: Event<*>
}

// dispatch events to handlers for a single cursor (here the compiler helps me if I do things wrong)
interface ICursorDispatcher<T: ICursorEvent> {
    fun dispatch(event: T): Result<*>
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
