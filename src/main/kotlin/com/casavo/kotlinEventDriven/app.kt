package com.casavo.kotlinEventDriven

class DataGatheringCursorRunner(
    private val eventStore: EventStoreService,
    private val cursor: Cursor,
    private val cursorDispatcher: DataGatheringCursorDispatcher
) {
    fun run(): Unit {
        eventStore.getNewEvents(cursor, 1)
            .forEach(::handle)
    }

    private fun handle(event: Event<*>): Unit {
        when (event) {
            is IDataGatheringCursorEvent -> when (cursorDispatcher.dispatch(event)) {
                //todo: and dlq?
                is EventHandler.EventResult.Failed -> toDlq(event) //todo: log warning
                is EventHandler.EventResult.Skip -> Unit //todo: log warning
                EventHandler.EventResult.Success -> Unit
            }

            else -> {} // todo: log warning
        }

        eventStore.acknowledgeEvent(event, cursor)
    }

    private fun toDlq(event: IDataGatheringCursorEvent): Unit = Unit //todo: implement DLQ
}
