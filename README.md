# Compukters: Create

Independent kinetics, programmable pumps and clutches, Stock Ticker logistics boiler and fluid tank integration for Minecraft 1.21.1.

Installation, upstream versions, Guest Kotlin examples, device lifetime and development commands are maintained in the
[canonical Wiki guide](https://certifiedbadideas.github.io/Compukters/CREATE/).

See [addon versioning](https://certifiedbadideas.github.io/Compukters/ADDON-DEVELOPMENT/#first-party-addon-versions)
for the independent `x.y` API/update version and Compukters target line in production JAR names. For combined
Create/Sable/Propulsion and Aeronautics development, use the
[all-addon stand](https://certifiedbadideas.github.io/Compukters/DEV-STAND/).


## Build dependencies

Local co-development explicitly uses `compuktersDependencyMode=workspace`. In the pinned main workspace,
run `./gradlew stageAddonDevelopmentDependencies` in Compukters first, then build this addon with `./gradlew build`.
For a separate checkout, pass `-PcompuktersWorkspace=/path/to/Compukters`.

To build without the main sources, use `./gradlew build -PcompuktersDependencyMode=release`.
This downloads the exact public GitHub SDK selected by `compuktersRelease`, verifies its SHA256 and uses
its local file repository. No GitHub token or external Compukters Maven service is needed.
An unpacked SDK can be supplied with `-PcompuktersDependencyMode=release -PcompuktersSdkDirectory=/path/to/sdk`.
The downloadable SDK is available starting with Compukters 0.5.1.
There is no automatic fallback between source and released dependencies.

Only pushed version tags publish this addon. Its release contains the production JAR, named development JAR,
Guest API bundle and SHA256 inventory. Ordinary commits produce no GitHub Releases.

## Fluid tank observations (API 2.2)

`create.fluids.FluidTank.named("fuel").state()` returns the native server tick, fluid registry ID
(empty string when empty), amount and capacity in millibuckets, and multiblock block count.
Every segment resolves the same controller. Capacity comes from the native fluid capability,
including compatible tank subclasses. Active boilers retain the separate `create.boiler.Boiler`
contract. Reshaping, replacement, unloading or loss of reachability invalidates an old handle.
This API reads stock; it never fills tanks, loads chunks or pumps fluid.

## Declarative Guest contracts

Guest API declarations live in `src/compuktersAddon/contracts`. The SDK generates the actual Guest Kotlin under
`build/generated/sources/compuktersGuest/kotlin` and packages those sources with the executable `.cagb` image.
Run `generateCompuktersGuestSources` to inspect the result. Check in `src/compuktersAddon/addon.lock` as ABI history;
only `updateCompuktersAddonAbiLock` changes selectors/tombstones after an intentional contract edit.
Unit-returning `@GuestCommand` methods get ordinary calls and batch command factories from the same declaration.
The generated host validates declared parameter constraints and derives command eligibility.
See the [SDK authoring guide](https://github.com/CertifiedBadIdeas/Compukters/blob/dev/docs/developers/addon-development.md).

API 3.0 requires the explicit starting index in `StockSnapshot.findItem(itemId, fromIndex)`; use `0` to search the entire snapshot.
