/*
 * The Compukters Developers
 * Copyright 2026 Vsevolod Petrov (lazyhat)
 * Licensed under the Apache License, Version 2.0.
 */

package create.logistics

import ru.lazyhat.compukters.api.contracts.GuestCall
import ru.lazyhat.compukters.api.contracts.GuestDevice
import ru.lazyhat.compukters.api.contracts.GuestRead
import ru.lazyhat.compukters.api.contracts.GuestResource

@GuestDevice("create:stock_ticker", bindings = "LogisticsBindings")
interface StockTicker {
    @GuestRead(binding = "snapshot")
    fun snapshot(): StockSnapshot
}

@GuestResource(bindings = "LogisticsBindings")
interface StockSnapshot {
    @GuestRead(binding = "snapshotSize")
    fun size(): Int

    @GuestRead(binding = "snapshotItemId")
    fun itemId(index: Int): String

    @GuestRead(binding = "snapshotDisplayName")
    fun displayName(index: Int): String

    @GuestRead(binding = "snapshotCount")
    fun count(index: Int): Int

    /** Returns the next exact item-ID match at or after [fromIndex], or -1 when none remains. */
    @GuestRead(binding = "findItem")
    fun findItem(
        itemId: String,
        fromIndex: Int,
    ): Int

    @GuestCall(binding = "request")
    fun request(
        index: Int,
        quantity: Int,
        address: String,
    ): Boolean

    @GuestCall(binding = "closeSnapshot")
    fun close(): Unit
}
