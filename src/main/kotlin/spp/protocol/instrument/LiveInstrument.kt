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
 * todo: description.
 *
 * @since 0.3.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
sealed class LiveInstrument {
    abstract val location: LiveSourceLocation
    abstract val condition: String?
    abstract val expiresAt: Long? //todo: can just use -1 like hitLimit?
    abstract val hitLimit: Int
    abstract val id: String?
    abstract val type: LiveInstrumentType
    abstract val applyImmediately: Boolean
    abstract val applied: Boolean
    abstract val pending: Boolean
    abstract val throttle: InstrumentThrottle?
    abstract val meta: Map<String, Any>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiveInstrument) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
