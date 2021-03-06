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

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable
import spp.protocol.artifact.exception.LiveStackTrace

/**
 * todo: description.
 *
 * @since 0.2.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
@Serializable
data class Log(
    @Serializable(with = InstantIso8601Serializer::class)
    val timestamp: Instant,
    val content: String,
    val level: String,
    val logger: String? = null,
    val thread: String? = null,
    val exception: LiveStackTrace? = null,
    val arguments: List<String> = listOf()
) {
    fun toFormattedMessage(): String {
        var arg = 0
        var formattedMessage = content
        while (formattedMessage.contains("{}")) {
            formattedMessage = formattedMessage.replaceFirst("{}", arguments[arg++])
        }
        return formattedMessage
    }
}
