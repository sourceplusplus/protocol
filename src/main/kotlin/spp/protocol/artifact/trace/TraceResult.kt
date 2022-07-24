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
data class TraceResult(
    val artifactQualifiedName: ArtifactQualifiedName,
    val artifactSimpleName: String? = null,
    val orderType: TraceOrderType,
    val start: Instant,
    val stop: Instant,
    val step: String,
    val traces: List<Trace>,
    val total: Int
) {
    fun mergeWith(traceResult: TraceResult): TraceResult {
        var result: TraceResult = traceResult
        require(artifactQualifiedName == result.artifactQualifiedName) { "Mismatching artifact qualified name" }
        require(orderType == result.orderType) { "Mismatching order type" }
        require(step == result.step) { "Mismatching step" }
        if (start < result.start) {
            result = result.copy(start = start)
        }
        if (stop > result.stop) {
            result = result.copy(stop = stop)
        }
        if (result.artifactSimpleName == null && artifactSimpleName != null) {
            result = result.copy(artifactSimpleName = artifactSimpleName)
        }
        val combinedTraces: MutableSet<Trace> = HashSet(traces)
        combinedTraces.addAll(result.traces)
        val finalTraces = ArrayList(combinedTraces).sortedWith(Comparator { t2: Trace, t1: Trace ->
            if (orderType == TraceOrderType.SLOWEST_TRACES) {
                return@Comparator t1.duration.compareTo(t2.duration)
            } else {
                return@Comparator t1.start.compareTo(t2.start)
            }
        })
        return result.copy(traces = finalTraces, total = finalTraces.size)
    }

    fun truncate(amount: Int): TraceResult {
        return if (traces.size > amount) {
            copy(traces = traces.subList(0, amount), total = traces.size)
        } else this
    }
}
