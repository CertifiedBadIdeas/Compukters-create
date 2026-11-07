/*
 * The Compukters Developers
 *
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package create.actuators

import ru.lazyhat.compukters.api.contracts.GuestCall
import ru.lazyhat.compukters.api.contracts.GuestDevice
import ru.lazyhat.compukters.api.contracts.GuestRead

/** A programmable mechanical pump; reversing flow does not reverse its kinetic input. */
@GuestDevice("create:pump", bindings = "ActuatorBindings")
interface Pump {
    @GuestRead(binding = "pumpReversed")
    fun reversed(): Boolean

    @GuestCall(binding = "setPumpReversed")
    fun setReversed(reversed: Boolean): Boolean
}

/** A normally disengaged programmable clutch. Closing the final handle disengages it. */
@GuestDevice("create:clutch", bindings = "ActuatorBindings")
interface Clutch {
    @GuestRead(binding = "clutchConnected")
    fun connected(): Boolean

    @GuestCall(binding = "setClutchConnected")
    fun setConnected(connected: Boolean): Boolean
}
