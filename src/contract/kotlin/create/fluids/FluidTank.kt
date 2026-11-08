/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */

package create.fluids

import ru.lazyhat.compukters.api.contracts.GuestDevice
import ru.lazyhat.compukters.api.contracts.GuestRead
import ru.lazyhat.compukters.api.contracts.GuestRecord

/** One native multiblock sample. Empty fluid uses an empty identifier; quantities are millibuckets. */
@GuestRecord
value class FluidTankState(
    val gameTick: Long,
    val fluid: String,
    val amountMb: Int,
    val capacityMb: Int,
    val blockCount: Int,
)

/** Read-only native Create tank, including compatible tank subclasses. Active boilers use Boiler instead. */
@GuestDevice("create:fluid_tank", bindings = "FluidTankBindings")
interface FluidTank {
    @GuestRead(binding = "fluidTankState")
    fun state(): FluidTankState
}
