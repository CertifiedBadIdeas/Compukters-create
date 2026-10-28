/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */

package create.fluids

import compukter.peripheral.Peripheral
import compukter.peripheral.TypedPeripheralProvider

/** One native multiblock sample. Empty fluid uses an empty identifier; quantities are millibuckets. */
public value class FluidTankState(
    public val gameTick: Long,
    public val fluid: String,
    public val amountMb: Int,
    public val capacityMb: Int,
    public val blockCount: Int,
)

/** Read-only native Create tank, including compatible tank subclasses. Active boilers use Boiler instead. */
public value class FluidTank internal constructor(private val handle: Int) : Peripheral {
    public companion object : TypedPeripheralProvider<FluidTank>("create:fluid_tank") {
        override fun wrap(handle: Int): FluidTank = FluidTank(handle)
    }

    public fun state(): FluidTankState = FluidTankBindings.fluidTankState(handle)
}

private object FluidTankBindings {
    external fun fluidTankAcquire(side: Int): Int
    external fun fluidTankAcquireByName(name: String): Int
    external fun fluidTankState(handle: Int): FluidTankState
}
