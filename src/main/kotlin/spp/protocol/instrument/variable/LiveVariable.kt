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
package spp.protocol.instrument.variable

import io.vertx.core.json.JsonObject

/**
 * todo: description.
 *
 * @since 0.3.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LiveVariable(
    val name: String,
    val value: Any,
    val lineNumber: Int = -1,
    val scope: LiveVariableScope? = null,
    val liveClazz: String? = null,
    val liveIdentity: String? = null,
    val presentation: String? = null
) {
    constructor(json: JsonObject) : this(
        json.getString("name"),
        json.getValue("value"),
        json.getInteger("lineNumber"),
        json.getString("scope")?.let { LiveVariableScope.valueOf(it) },
        json.getString("liveClazz"),
        json.getString("liveIdentity"),
        json.getString("presentation")
    )
}
