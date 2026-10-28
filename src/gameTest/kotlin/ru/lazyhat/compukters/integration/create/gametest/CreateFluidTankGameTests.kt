/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */
package ru.lazyhat.compukters.integration.create.gametest

import com.simibubi.create.AllBlocks
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate
import ru.lazyhat.compukters.impl.registry.CompuktersRegistry

@PrefixGameTestTemplate(false)
object CreateFluidTankGameTests {
    @JvmStatic
    @GameTest(batch = "create_tank", template = "bastion/mobs/empty", templateNamespace = "minecraft", timeoutTicks = 100000)
    fun ordinaryTankReadingsAndReshape(helper: GameTestHelper) {
        val computer = BlockPos(2, 3, 3)
        val base = BlockPos(5, 2, 3)
        helper.setBlock(computer, CompuktersRegistry.COMPUTER.get())
        repeat(3) { helper.setBlock(base.above(it), AllBlocks.FLUID_TANK.get()) }
        val guest = GuestComputerScenario(helper, computer)
        val sequence = helper.startSequence()
        sequence
            .thenWaitUntil {
                helper.assertTrue((helper.getBlockEntity(base) as FluidTankBlockEntity).totalTankSize == 3, "Tank has not formed")
            }.thenExecute {
                val handler = checkNotNull(helper.level.getCapability(Capabilities.FluidHandler.BLOCK, helper.absolutePos(base), null))
                helper.assertTrue(
                    handler.fill(FluidStack(Fluids.WATER, 12000), IFluidHandler.FluidAction.EXECUTE) == 12000,
                    "Cannot fill tank",
                )
                PeripheralNetworkGameTestFixtures.bind(helper, helper.absolutePos(computer), helper.absolutePos(base.above()))
                CreatePeripheralGameTests.nameDevice(helper, base.above(2), "fuel")
            }
        guest.prepare(
            sequence,
            """
            import compukter.terminal.Terminal
            import create.fluids.FluidTank
            fun main() {
                val tank = FluidTank.named("fuel")
                check(FluidTank.all().size == 1)
                val state = tank.state()
                check(state.gameTick > 0L)
                check(state.fluid == "minecraft:water")
                check(state.amountMb == 12000 && state.capacityMb == 24000 && state.blockCount == 3)
                Terminal.write("tank-acquired\n")
                readln()
                var stale = false
                try { tank.state() } catch (e: compukter.io.IOException) { stale = true }
                check(stale)
                val replacement = FluidTank.named("fuel").state()
                check(replacement.blockCount == 2 && replacement.capacityMb == 16000)
                Terminal.write("tank-reshaped\n")
                readln()
            }
            """.trimIndent(),
        )
        guest.awaitMarker(sequence, "tank-acquired")
        sequence.thenExecute { helper.setBlock(base.above(2), Blocks.AIR) }.thenWaitUntil {
            helper.assertTrue((helper.getBlockEntity(base) as FluidTankBlockEntity).totalTankSize == 2, "Tank did not shrink")
        }
        guest.resume(sequence)
        guest.awaitMarker(sequence, "tank-reshaped")
        guest.stop(sequence)
        sequence.thenSucceed()
    }
}
