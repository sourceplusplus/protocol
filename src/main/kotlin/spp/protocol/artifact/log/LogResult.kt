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
package spp.protocol.artifact.log

import spp.protocol.artifact.ArtifactQualifiedName
import java.time.Instant

/**
 * todo: description.
 *
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LogResult(
    val artifactQualifiedName: ArtifactQualifiedName? = null,
    val orderType: LogOrderType,
    val timestamp: Instant,
    val logs: List<Log> = emptyList(),
    val total: Int = 0
) {
    fun mergeWith(logResult: LogResult): LogResult {
        val result: LogResult = logResult
        val combinedLogs: MutableSet<Log> = HashSet(logs)
        combinedLogs.addAll(result.logs)
        val finalLogs = ArrayList(combinedLogs).sortedWith(Comparator { l1: Log, l2: Log ->
            when (orderType) {
                LogOrderType.NEWEST_LOGS -> return@Comparator l2.timestamp.compareTo(l1.timestamp)
                LogOrderType.OLDEST_LOGS -> return@Comparator l1.timestamp.compareTo(l2.timestamp)
            }
        })
        return result.copy(logs = finalLogs, total = finalLogs.size)
    }

    fun truncate(amount: Int): LogResult {
        return if (logs.size > amount) {
            copy(logs = logs.subList(0, amount), total = logs.size)
        } else this
    }
}
