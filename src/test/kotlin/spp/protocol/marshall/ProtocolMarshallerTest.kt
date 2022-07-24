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
package spp.protocol.marshall

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import spp.protocol.artifact.exception.LiveStackTrace
import spp.protocol.artifact.exception.LiveStackTraceElement
import spp.protocol.instrument.LiveBreakpoint
import spp.protocol.instrument.LiveLog
import spp.protocol.instrument.LiveMeter
import spp.protocol.instrument.LiveSourceLocation
import spp.protocol.instrument.command.CommandType
import spp.protocol.instrument.command.LiveInstrumentCommand
import spp.protocol.instrument.event.LiveBreakpointHit
import spp.protocol.instrument.event.LiveInstrumentRemoved
import spp.protocol.instrument.meter.MeterType
import spp.protocol.instrument.meter.MetricValue
import spp.protocol.instrument.meter.MetricValueType
import spp.protocol.instrument.throttle.InstrumentThrottle
import spp.protocol.instrument.throttle.ThrottleStep
import spp.protocol.instrument.variable.LiveVariable
import spp.protocol.instrument.variable.LiveVariableScope
import java.time.Instant

@RunWith(JUnit4::class)
class ProtocolMarshallerTest {

    @Test
    fun testLiveInstrument() {
        val liveInstrument = LiveMeter(
            "meterName",
            MeterType.COUNT,
            MetricValue(MetricValueType.NUMBER, "1"),
            LiveSourceLocation("source", 1),
            "condition",
            System.currentTimeMillis(),
            1,
            "id",
            applyImmediately = false,
            applied = true,
            pending = false,
            InstrumentThrottle(5, ThrottleStep.SECOND),
            mapOf("key" to "value")
        )

        val serialized = ProtocolMarshaller.serializeLiveInstrument(liveInstrument)
        val deserialized = ProtocolMarshaller.deserializeLiveInstrument(serialized)
        assertEquals(liveInstrument, deserialized)
    }

    @Test
    fun testLiveInstrumentCommand() {
        val liveInstrumentCommand = LiveInstrumentCommand(
            CommandType.ADD_LIVE_INSTRUMENT,
            setOf(
                LiveBreakpoint(
                    LiveSourceLocation("source", 1),
                    "condition",
                    System.currentTimeMillis(),
                    1,
                    "id",
                    applyImmediately = false,
                    applied = true,
                    pending = false,
                    InstrumentThrottle(5, ThrottleStep.SECOND),
                    mapOf("key" to "value")
                )
            ),
            setOf(
                LiveSourceLocation("source", 1)
            )
        )

        val serialized = ProtocolMarshaller.serializeLiveInstrumentCommand(liveInstrumentCommand)
        val deserialized = ProtocolMarshaller.deserializeLiveInstrumentCommand(serialized)
        assertEquals(liveInstrumentCommand, deserialized)
    }

    @Test
    fun testLiveInstrumentRemoved() {
        val liveInstrumentRemoved = LiveInstrumentRemoved(
            LiveLog(
                "log {}",
                listOf("foo"),
                LiveSourceLocation("source", 2),
                "foo == bar",
                System.currentTimeMillis(),
                2,
                "id",
                applyImmediately = true,
                applied = false,
                pending = true,
                InstrumentThrottle(1, ThrottleStep.DAY),
                mapOf("key2" to "value2")
            ),
            Instant.now(),
            LiveStackTrace("exception", "message", mutableListOf())
        )

        val serialized = ProtocolMarshaller.serializeLiveInstrumentRemoved(liveInstrumentRemoved)
        val deserialized = ProtocolMarshaller.deserializeLiveInstrumentRemoved(serialized)
        assertEquals(liveInstrumentRemoved, deserialized)
    }

    @Test
    fun testLiveBreakpointHit() {
        val liveBreakpointHit = LiveBreakpointHit(
            "breakpointId",
            "traceId",
            Instant.now(),
            "serviceInstance",
            "service",
            LiveStackTrace(
                "exception",
                "message",
                mutableListOf(
                    LiveStackTraceElement(
                        "method",
                        "source",
                        mutableListOf(
                            LiveVariable(
                                "name",
                                "value",
                                1,
                                LiveVariableScope.GLOBAL_VARIABLE,
                                "liveClazz",
                                "liveIdentity",
                                "presentation"
                            )
                        ),
                        "sourceCode"
                    )
                ),
                LiveStackTrace(
                    "exception",
                    "message",
                    mutableListOf(
                        LiveStackTraceElement(
                            "method",
                            "source",
                            mutableListOf(
                                LiveVariable(
                                    "name",
                                    "value",
                                    1,
                                    LiveVariableScope.GLOBAL_VARIABLE,
                                    "liveClazz",
                                    "liveIdentity",
                                    "presentation"
                                )
                            ),
                            "sourceCode"
                        )
                    )
                )
            )
        )

        val serialized = ProtocolMarshaller.serializeLiveBreakpointHit(liveBreakpointHit)
        val deserialized = ProtocolMarshaller.deserializeLiveBreakpointHit(serialized)
        assertEquals(liveBreakpointHit, deserialized)
    }
}
