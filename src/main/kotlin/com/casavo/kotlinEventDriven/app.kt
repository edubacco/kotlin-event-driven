package com.casavo.kotlinEventDriven

class DataGatheringCursorRunner(
    private val eventStore: EventStoreService,
    private val cursor: DataGatheringCursor,
    private val cursorDispatcher: DataGatheringCursorDispatcher
) {
    fun run(): Unit {
        eventStore.getNewEvents(cursor, 1)
            .forEach(::handle)
    }

    private fun handle(event: Event<*>): Unit {
        when (event) {
            //FIXME: can I pass the interface from outside? to do this, you should abstract the whole class
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
