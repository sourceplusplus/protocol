/*
 * Source++, the open-source live coding platform.
 * Copyright (C) 2022 CodeBrig, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spp.protocol.instrument.event

import spp.protocol.artifact.exception.LiveStackTrace
import java.time.Instant

/**
 * todo: description.
 *
 * @since 0.3.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
data class LiveBreakpointHit(
    val breakpointId: String,
    val traceId: String,
    override val occurredAt: Instant,
    val serviceInstance: String,
    val service: String,
    val stackTrace: LiveStackTrace
) : TrackedLiveEvent {
    val eventType: LiveInstrumentEventType = LiveInstrumentEventType.BREAKPOINT_HIT
}
