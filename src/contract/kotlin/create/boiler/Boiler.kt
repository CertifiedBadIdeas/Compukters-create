/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */

package create.boiler

import ru.lazyhat.compukters.api.contracts.GuestDevice
import ru.lazyhat.compukters.api.contracts.GuestCall

/** A handle to one active Create boiler and its current Fluid Tank controller. */
@GuestDevice("create:boiler", bindings = "BoilerBindings")
interface Boiler {
    /** Create's sampled water input rate, in millibuckets per tick. */
    @GuestCall(binding = "waterSupply")
    fun waterSupply(): Float

    /** Water contribution to the boiler level, from zero through 18. */
    @GuestCall(binding = "waterLevel")
    fun waterLevel(): Int

    /** Create's heat gauge before water and tank-size limits. Passive heat reports one. */
    @GuestCall(binding = "heatLevel")
    fun heatLevel(): Int

    /** Effective active boiler level, from zero through 18. Passive heat reports zero. */
    @GuestCall(binding = "level")
    fun level(): Int

    /** Whether Create currently treats the boiler as passively heated. */
    @GuestCall(binding = "isPassive")
    fun isPassive(): Boolean
}
