package com.casavo.kotlinEventDriven

import com.casavo.kotlinEventDriven.EventHandler.EventResult

data class AppointmentCreatedPayload(private val test: String) : EventPayload()
data class AppointmentCreated(
    override val type: String,
    override val offset: Offset,
    override val payload: AppointmentCreatedPayload
) :
    Event<AppointmentCreatedPayload>(
        type, offset,
        payload
    ), IDataGatheringCursorEvent

data class AppointmentDeletedPayload(private val test: String) : EventPayload()

data class AppointmentDeleted(
    override val type: String,
    override val offset: Event.Offset,
    override val payload: AppointmentDeletedPayload
) : Event<AppointmentDeletedPayload>(type, offset, payload), IDataGatheringCursorEvent

data class OffersBundleRefusalExpressedPayload(private val test: String): EventPayload()
data class OffersBundleRefusalExpressed(
    override val type: String,
    override val offset: Event.Offset,
    override val payload: OffersBundleRefusalExpressedPayload
) : Event<OffersBundleRefusalExpressedPayload>(type, offset, payload), IOfferCursorEvent

class AppointmentCreatedHandler : EventHandler<AppointmentCreated> {
    override operator fun invoke(event: AppointmentCreated): EventResult = EventResult.Success
}

class AppointmentDeletedHandler : EventHandler<AppointmentDeleted> {
    override operator fun invoke(event: AppointmentDeleted): EventResult = EventResult.Success
}

class OffersBundleRefusalExpressedHandler : EventHandler<OffersBundleRefusalExpressed> {
    override fun invoke(event: OffersBundleRefusalExpressed): EventResult = EventResult.Success
}
