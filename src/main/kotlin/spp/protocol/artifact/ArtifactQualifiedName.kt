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
package spp.protocol.artifact

/**
 * todo: description.
 *
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class ArtifactQualifiedName(
    val identifier: String,
    val commitId: String? = null,
    val type: ArtifactType,
    val lineNumber: Int? = null,
    val operationName: String? = null //todo: only method artifacts need
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArtifactQualifiedName) return false
        if (identifier != other.identifier) return false
        if (commitId != other.commitId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = identifier.hashCode()
        result = 31 * result + (commitId?.hashCode() ?: 0)
        return result
    }
}
