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
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@Ignore
@RunWith(JUnit4::class)
class TraceStackTest {

    @Test
    fun `dual segment trace stack`() {
        val jsonTraceStack = JsonArray(
            Resources.toString(Resources.getResource("dualSegmentTraceStack.json"), Charsets.UTF_8)
        )
        val traceSpans = mutableListOf<TraceSpan>()
        for (i in 0 until jsonTraceStack.size()) {
            traceSpans.add(Json.decodeValue(jsonTraceStack.getJsonObject(i).toString(), TraceSpan::class.java))
        }
        val traceStack = TraceStack(traceSpans)
        assertEquals(2, traceStack.segments)

        val exitSegment = traceStack.getSegment("4dc611c1901e4f9db1c6cc3a8d1bed45.73.16054873308541224")
        assertEquals(1, exitSegment.size)
        assertEquals(1, exitSegment.depth)
        assertEquals(0, exitSegment.getChildren(0).size)
        assertNull(exitSegment.getParent(0))

        val entrySegment = traceStack.getSegment("4dc611c1901e4f9db1c6cc3a8d1bed45.59.16054873308541420")
        assertEquals(5, entrySegment.size)
        assertEquals(3, entrySegment.depth)
        assertEquals(1, entrySegment.getChildren(0).size)
        assertEquals(3, entrySegment.getChildren(1).size)
        assertEquals(0, entrySegment.getChildren(2).size)
        assertEquals(0, entrySegment.getChildren(3).size)
        assertEquals(0, entrySegment.getChildren(4).size)
        assertNull(entrySegment.getParent(0))
        assertEquals(0, entrySegment.getParent(1)!!.spanId)
        assertEquals(1, entrySegment.getParent(2)!!.spanId)
        assertEquals(1, entrySegment.getParent(3)!!.spanId)
        assertEquals(1, entrySegment.getParent(4)!!.spanId)
    }
}
