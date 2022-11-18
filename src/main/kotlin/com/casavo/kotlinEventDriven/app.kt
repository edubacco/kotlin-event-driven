package com.casavo.kotlinEventDriven

class DataGatheringCursorRunner(
    private val eventStore: EventStoreService,
    private val cursor: Cursor,
    private val cursorDispatcher: DataGatheringCursorDispatcher
) {
    fun run(): Unit {
        eventStore.getNewEvents(cursor, 1)
            .forEach { event ->
                cursorDispatcher.dispatch(event.toDataGatheringEvent())
                    .onFailure { }
                    .onSuccess { }

                eventStore.acknowledgeEvent(event, cursor)
            }
    }
}

@Suppress("UNCHECKED_CAST")
private fun Event<*>.toDataGatheringEvent(): DataGatheringCursorEvent = when (this.type) {
    is AppointmentCreated -> DataGatheringCursorEvent.DataGatheringCursorAppointmentCreated(this as AppointmentCreated)
    is AppointmentDeleted -> DataGatheringCursorEvent.DataGatheringCursorAppointmentDeleted(this as AppointmentDeleted)
}
