/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */
package ru.lazyhat.compukters.integration.create

import create.FluidTankState
import ru.lazyhat.compukters.api.addon.AddonCallResult
import ru.lazyhat.compukters.lang.runtime.vm.HostFailureKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class FluidTankHostStateTest {
    @Test
    fun `segments share a controller and report current native capacity without assuming normal tanks`() {
        val tank = FakeTank(FluidTankState(10L, "fuel:test", 12000, 720000, 45))
        val host =
            CreateHostState(
                resolveSide = { _, kind -> tank.takeIf { kind == PeripheralKind.FLUID_TANK } },
                resolveName = { _, _ -> CreateNamedResolution.Found(tank) },
            )
        val handle = host.fluidTankAcquire(0).value()
        assertEquals(handle, host.fluidTankAcquire(1).value())
        assertEquals(handle, host.fluidTankAcquireByName("pod").value())
        assertEquals(tank.sample, host.fluidTankState(handle).value())
        tank.sample = FluidTankState(20L, "", 0, 720000, 45)
        assertEquals(tank.sample, host.fluidTankState(handle).value())
    }

    @Test
    fun `stale controller never binds a replacement and reshape requires a new handle`() {
        val identity = Any()
        val original = FakeTank(FluidTankState(1L, "", 0, 16000, 1), identity)
        var current = original
        val host = CreateHostState { _, kind -> current.takeIf { kind == PeripheralKind.FLUID_TANK } }
        val first = host.fluidTankAcquire(0).value()
        original.present = false
        assertEquals(HostFailureKind.INPUT_OUTPUT, assertIs<AddonCallResult.Failed>(host.fluidTankState(first)).kind)
        current = FakeTank(FluidTankState(2L, "", 0, 64000, 4), identity)
        val second = host.fluidTankAcquire(0).value()
        assertNotEquals(first, second)
        assertEquals(HostFailureKind.UNAVAILABLE, assertIs<AddonCallResult.Failed>(host.fluidTankState(first)).kind)
        assertEquals(64000, host.fluidTankState(second).value().capacityMb)
    }

    @Test
    fun `shared peripheral handle resolves using the tank contract`() {
        val tank = FakeTank(FluidTankState(123L, "fuel:test", 5, 8000, 1))
        val host =
            CreateHostState(
                resolveSide = { _, _ -> null },
                resolveName = { _, _ -> CreateNamedResolution.Failed(HostFailureKind.UNAVAILABLE, "missing") },
                acquireName = { _, kind ->
                    assertEquals(PeripheralKind.FLUID_TANK, kind)
                    42
                },
                resolveHandle = { handle, kind ->
                    assertEquals(42, handle)
                    assertEquals(PeripheralKind.FLUID_TANK, kind)
                    tank
                },
            )
        assertEquals(42, host.fluidTankAcquireByName("pod").value())
        assertEquals(tank.sample, host.fluidTankState(42).value())
    }

    private fun <T> AddonCallResult<T>.value(): T = assertIs<AddonCallResult.Completed<T>>(this).value

    private class FakeTank(
        var sample: FluidTankState,
        override val identity: Any = Any(),
    ) : FluidTankAccess {
        var present = true

        override fun valid(): Boolean = present

        override fun state(): FluidTankState = sample
    }
}
