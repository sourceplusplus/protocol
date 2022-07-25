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

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import spp.protocol.instrument.throttle.InstrumentThrottle

/**
 * A live log represents a textual message.
 *
 * @since 0.3.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LiveLog(
    val logFormat: String,
    val logArguments: List<String> = emptyList(),
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
    override val type: LiveInstrumentType = LiveInstrumentType.LOG

    constructor(json: JsonObject) : this(
        logFormat = json.getString("logFormat"),
        logArguments = json.getJsonArray("logArguments").map { it.toString() },
        location = LiveSourceLocation(json.getJsonObject("location")),
        condition = json.getString("condition"),
        expiresAt = json.getLong("expiresAt"),
        hitLimit = json.getInteger("hitLimit"),
        id = json.getString("id"),
        applyImmediately = json.getBoolean("applyImmediately"),
        applied = json.getBoolean("applied"),
        pending = json.getBoolean("pending"),
        throttle = InstrumentThrottle(json.getJsonObject("throttle")),
        meta = json.getJsonObject("meta").associate { it.key to it.value }
    )

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.put("logFormat", logFormat)
        json.put("logArguments", JsonArray().apply { logArguments.forEach { add(it) } })
        json.put("location", location.toJson())
        json.put("condition", condition)
        json.put("expiresAt", expiresAt)
        json.put("hitLimit", hitLimit)
        json.put("id", id)
        json.put("applyImmediately", applyImmediately)
        json.put("applied", applied)
        json.put("pending", pending)
        json.put("throttle", throttle.toJson())
        json.put("meta", JsonObject(meta))
        return json
    }

    /**
     * Specify explicitly so Kotlin doesn't override.
     */
    override fun hashCode(): Int {
        return super.hashCode()
    }
}
