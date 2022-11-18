package com.casavo.kotlinEventDriven

object DataGatheringCursor : Cursor {
    val name: Cursor.Name = Cursor.Name("DataGatheringCursor")
}

sealed interface IDataGatheringCursorEvent: ICursorEvent
sealed interface IOfferCursorEvent: ICursorEvent

class DataGatheringCursorDispatcher(
    private val appointmentCreatedHandler: AppointmentCreatedHandler,
    private val appointmentDeletedHandler: AppointmentDeletedHandler,
) : ICursorDispatcher<IDataGatheringCursorEvent> {
    override fun dispatch(event: IDataGatheringCursorEvent): EventHandler.EventResult = when (event) {
        is AppointmentCreated -> appointmentCreatedHandler(event)
        is AppointmentDeleted -> appointmentDeletedHandler(event)
    }
}
