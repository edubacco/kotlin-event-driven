package com.casavo.kotlinEventDriven

object AppointmentCreatedType : EventType("AppointmentCreated")
object AppointmentDeletedType : EventType("AppointmentDeleted")

class AppointmentCreatedHandler : EventHandler<AppointmentCreatedType> {
    override operator fun invoke(event: Event<AppointmentCreatedType>): Result<*> = Result.success(Unit)
}

class AppointmentDeletedHandler : EventHandler<AppointmentDeletedType> {
    override operator fun invoke(event: Event<AppointmentDeletedType>): Result<*> = Result.success(Unit)
}