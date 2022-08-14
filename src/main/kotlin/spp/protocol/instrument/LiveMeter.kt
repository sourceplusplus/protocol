/*
 * Source++, the open-source live coding platform.
 * Copyright (C) 2022 CodeBrig, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spp.protocol.instrument

import io.vertx.core.json.JsonObject
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

    constructor(json: JsonObject) : this(
        json.getString("meterName"),
        MeterType.valueOf(json.getString("meterType")),
        MetricValue(json.getJsonObject("metricValue")),
        LiveSourceLocation(json.getJsonObject("location")),
        json.getString("condition"),
        json.getLong("expiresAt"),
        json.getInteger("hitLimit"),
        json.getString("id"),
        json.getBoolean("applyImmediately"),
        json.getBoolean("applied"),
        json.getBoolean("pending"),
        json.getJsonObject("throttle")?.let { InstrumentThrottle(it) },
        json.getJsonObject("meta")?.associate { it.key to it.value } ?: emptyMap()
    )

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.put("meterName", meterName)
        json.put("meterType", meterType.name)
        json.put("metricValue", metricValue.toJson())
        json.put("location", location.toJson())
        json.put("condition", condition)
        json.put("expiresAt", expiresAt)
        json.put("hitLimit", hitLimit)
        json.put("id", id)
        json.put("applyImmediately", applyImmediately)
        json.put("applied", applied)
        json.put("pending", pending)
        json.put("throttle", throttle?.toJson())
        json.put("meta", JsonObject(meta))
        return json
    }

    fun toMetricIdWithoutPrefix(): String = meterType.name.lowercase() + "_" + id!!.replace("-", "_")
    fun toMetricId(): String = "spp_" + toMetricIdWithoutPrefix()

    /**
     * Specify explicitly so Kotlin doesn't override.
     */
    override fun hashCode(): Int = super.hashCode()
}
