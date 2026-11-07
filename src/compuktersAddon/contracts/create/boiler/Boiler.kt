/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */

package create.boiler

import ru.lazyhat.compukters.api.contracts.GuestDevice
import ru.lazyhat.compukters.api.contracts.GuestRead

/** A handle to one active Create boiler and its current Fluid Tank controller. */
@GuestDevice("create:boiler", bindings = "BoilerBindings")
interface Boiler {
    /** Create's sampled water input rate, in millibuckets per tick. */
    @GuestRead(binding = "waterSupply")
    fun waterSupply(): Float

    /** Water contribution to the boiler level, from zero through 18. */
    @GuestRead(binding = "waterLevel")
    fun waterLevel(): Int

    /** Create's heat gauge before water and tank-size limits. Passive heat reports one. */
    @GuestRead(binding = "heatLevel")
    fun heatLevel(): Int

    /** Effective active boiler level, from zero through 18. Passive heat reports zero. */
    @GuestRead(binding = "level")
    fun level(): Int

    /** Whether Create currently treats the boiler as passively heated. */
    @GuestRead(binding = "isPassive")
    fun isPassive(): Boolean
}
