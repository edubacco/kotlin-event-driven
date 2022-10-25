package com.casavo.kotlinEventDriven

import com.casavo.kotlinEventDriven.DataGatheringCursorEvent.DataGatheringCursorAppointmentCreated
import com.casavo.kotlinEventDriven.DataGatheringCursorEvent.DataGatheringCursorAppointmentDeleted

object DataGatheringCursor : Cursor {
    val name: Cursor.Name = Cursor.Name("DataGatheringCursor")
}

sealed class DataGatheringCursorEvent(override val event: Event<*>) : ICursorEvent {
    class DataGatheringCursorAppointmentCreated(override val event: Event<AppointmentCreatedType>) :
        DataGatheringCursorEvent(event)

    class DataGatheringCursorAppointmentDeleted(override val event: Event<AppointmentDeletedType>) :
        DataGatheringCursorEvent(event)
}

class DataGatheringCursorDispatcher(
    private val appointmentCreatedHandler: AppointmentCreatedHandler,
    private val appointmentDeletedHandler: AppointmentDeletedHandler,
) : ICursorDispatcher<DataGatheringCursorEvent> {
    override fun dispatch(event: DataGatheringCursorEvent) = when (event) {
        is DataGatheringCursorAppointmentCreated -> appointmentCreatedHandler(event.event)
        is DataGatheringCursorAppointmentDeleted -> appointmentDeletedHandler(event.event)
    }
}
