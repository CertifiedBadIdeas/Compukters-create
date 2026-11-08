/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */

package create.kinetics

import ru.lazyhat.compukters.api.contracts.GuestCall
import ru.lazyhat.compukters.api.contracts.GuestDevice
import ru.lazyhat.compukters.api.contracts.GuestRead
import ru.lazyhat.compukters.api.contracts.GuestWait

@GuestDevice("create:speedometer", bindings = "KineticsBindings")
interface Speedometer {
    @GuestRead(binding = "speed")
    fun speed(): Float

    @GuestWait(binding = "awaitSpeedChange")
    fun awaitSpeedChange(): Float
}

@GuestDevice("create:stressometer", bindings = "KineticsBindings")
interface Stressometer {
    @GuestRead(binding = "stress")
    fun stress(): Float

    @GuestRead(binding = "capacity")
    fun capacity(): Float

    @GuestWait(binding = "awaitStressChange")
    fun awaitChange(): Unit
}

@GuestDevice("create:rotation_controller", bindings = "KineticsBindings")
interface RotationController {
    @GuestRead(binding = "targetSpeed")
    fun targetSpeed(): Int

    @GuestCall(binding = "setTargetSpeed")
    fun setTargetSpeed(speed: Int): Int
}
