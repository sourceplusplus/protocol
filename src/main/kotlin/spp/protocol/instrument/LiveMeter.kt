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

import spp.protocol.instrument.meter.MeterType
import spp.protocol.instrument.meter.MetricValue
import spp.protocol.instrument.throttle.InstrumentThrottle

/**
 * A live meter represents a metric that is measured continuously over time.
 *
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LiveMeter(
    val meterName: String,
    val meterType: MeterType,
    val metricValue: MetricValue,
    override val location: LiveSourceLocation,
    override val condition: String? = null,
    override val expiresAt: Long? = null,
    override val hitLimit: Int = -1,
    override val id: String? = null,
    override val applyImmediately: Boolean = false,
    override val applied: Boolean = false,
    override val pending: Boolean = false,
    override val throttle: InstrumentThrottle? = null,
    override val meta: Map<String, Any> = emptyMap()
) : LiveInstrument() {
    override val type: LiveInstrumentType = LiveInstrumentType.METER

    fun toMetricIdWithoutPrefix(): String = meterType.name.lowercase() + "_" + id!!.replace("-", "_")
    fun toMetricId(): String = "spp_" + toMetricIdWithoutPrefix()

    /**
     * Specify explicitly so Kotlin doesn't override.
     */
    override fun hashCode(): Int = super.hashCode()
}
