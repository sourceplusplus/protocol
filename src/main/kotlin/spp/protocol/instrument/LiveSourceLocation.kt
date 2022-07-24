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

import kotlin.jvm.JvmOverloads

/**
 * todo: description.
 *
 * @since 0.3.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LiveSourceLocation @JvmOverloads constructor(
    val source: String,
    val line: Int = -1,
    val service: String? = null, //todo: can use Service
    val serviceInstance: String? = null, //todo: fully impl
    val commitId: String? = null, //todo: impl
    val fileChecksum: String? = null, //todo: impl
    //val language: ArtifactLanguage? = null, //todo: impl
) : Comparable<LiveSourceLocation> {

    override fun compareTo(other: LiveSourceLocation): Int {
        val sourceCompare = source.compareTo(other.source)
        if (sourceCompare != 0) return sourceCompare
        return line.compareTo(other.line)
    }
}
