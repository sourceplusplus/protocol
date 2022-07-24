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

import com.google.common.io.Resources
import io.vertx.core.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import spp.protocol.artifact.exception.LiveStackTrace

class LiveStackTraceTest {

    @Test
    fun parseJsonJVMStackTrace() {
        val jsonObj = JsonObject(
            Resources.toString(Resources.getResource("serialized-service-exception.json"), Charsets.UTF_8)
        ).getString("cause")
        val stackTrace = LiveStackTrace.fromString(jsonObj)
        assertNotNull(stackTrace)
    }

    @Test
    fun parsePythonStackTrace() {
        val stackTrace = LiveStackTrace.fromString(
            Resources.toString(Resources.getResource("pythonStackTrace.txt"), Charsets.UTF_8)
        )
        assertNotNull(stackTrace)
        assertEquals(23, stackTrace!!.elements.size)
        assertEquals(16, stackTrace.getElements(true).size)
    }
}
