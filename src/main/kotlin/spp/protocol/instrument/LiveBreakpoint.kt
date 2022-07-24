/*
 * Source++, the open-source live coding platform.
 * Copyright (C) 2022 CodeBrig, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package spp.protocol.instrument

import spp.protocol.instrument.throttle.InstrumentThrottle

/**
 * A live breakpoint represents a non-breaking breakpoint.
 *
 * @since 0.3.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LiveBreakpoint(
    override val location: LiveSourceLocation,
    override val condition: String? = null,
    override val expiresAt: Long? = null,
    override val hitLimit: Int = 1,
    override val id: String? = null,
    override val applyImmediately: Boolean = false,
    override val applied: Boolean = false,
    override val pending: Boolean = false,
    override val throttle: InstrumentThrottle = InstrumentThrottle.DEFAULT,
    override val meta: Map<String, Any> = emptyMap()
) : LiveInstrument() {
    override val type: LiveInstrumentType = LiveInstrumentType.BREAKPOINT

    /**
     * Specify explicitly so Kotlin doesn't override.
     */
    override fun hashCode(): Int {
        return super.hashCode()
    }
}
