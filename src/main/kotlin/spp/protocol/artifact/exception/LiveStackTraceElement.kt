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
package spp.protocol.artifact.exception

import spp.protocol.artifact.ArtifactNameUtils.getShortQualifiedClassName
import spp.protocol.instrument.variable.LiveVariable

/**
 * todo: description.
 *
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LiveStackTraceElement(
    val method: String,
    val source: String,
    val variables: MutableList<LiveVariable> = mutableListOf(),
    var sourceCode: String? = null
) {
    override fun toString(): String = toString(false)

    fun toString(shorten: Boolean): String {
        return if (shorten) {
            val shortName = "${shortQualifiedClassName()}.${methodName()}"
            val lineNumber = sourceAsLineNumber()
            if (lineNumber != null) {
                "at $shortName() line: $lineNumber"
            } else {
                "at $shortName($source)"
            }
        } else {
            "at $method($source)"
        }
    }
}

fun LiveStackTraceElement.sourceAsFilename(): String? {
    return if (source.contains(":")) {
        source.substring(0, source.indexOf(":"))
    } else {
        null
    }
}

fun LiveStackTraceElement.sourceAsLineNumber(): Int? {
    return if (source.contains(":")) {
        source.substring(source.indexOf(":") + 1).toInt()
    } else {
        null
    }
}

fun LiveStackTraceElement.qualifiedClassName(): String {
    return method.substring(0, method.lastIndexOf("."))
}

fun LiveStackTraceElement.shortQualifiedClassName(): String {
    return getShortQualifiedClassName(qualifiedClassName())
}

fun LiveStackTraceElement.methodName(): String {
    return if (method.contains("$")) {
        method.substring(method.lastIndexOf(".") + 1).substringBefore("$")
    } else {
        method.substring(method.lastIndexOf(".") + 1)
    }
}
