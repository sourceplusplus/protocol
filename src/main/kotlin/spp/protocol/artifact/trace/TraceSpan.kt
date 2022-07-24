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
package spp.protocol.artifact.trace

import spp.protocol.artifact.ArtifactQualifiedName
import java.time.Instant

/**
 * todo: description.
 *
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class TraceSpan(
    val traceId: String,
    val segmentId: String,
    val spanId: Int,
    val parentSpanId: Int,
    val refs: List<TraceSpanRef> = emptyList(),
    val serviceCode: String,
    val serviceInstanceName: String? = null,
    val startTime: Instant,
    val endTime: Instant,
    val endpointName: String? = null,
    val artifactQualifiedName: ArtifactQualifiedName? = null,
    val type: String,
    val peer: String? = null,
    val component: String? = null,
    val error: Boolean? = null,
    val childError: Boolean = false,
    val hasChildStack: Boolean? = null,
    val layer: String? = null,
    val tags: Map<String, String> = emptyMap(),
    val logs: List<TraceSpanLogEntry> = emptyList(),
    val meta: MutableMap<String, String> = mutableMapOf()
) {
    fun putMetaInt(tag: String, value: Int) {
        meta[tag] = value.toString()
    }

    fun putMetaLong(tag: String, value: Long) {
        meta[tag] = value.toString()
    }

    fun putMetaDouble(tag: String, value: Double) {
        meta[tag] = value.toString()
    }

    fun putMetaString(tag: String, value: String) {
        meta[tag] = value
    }

    fun getMetaInt(tag: String): Int? {
        return meta[tag]?.toIntOrNull()
    }

    fun getMetaLong(tag: String): Long {
        return meta[tag]!!.toLong()
    }

    fun getMetaDouble(tag: String): Double {
        return meta[tag]!!.toDouble()
    }

    fun getMetaString(tag: String): String {
        return meta[tag]!!
    }
}
