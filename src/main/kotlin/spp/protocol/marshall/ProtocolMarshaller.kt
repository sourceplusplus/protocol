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

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import spp.protocol.artifact.ArtifactQualifiedName
import spp.protocol.artifact.exception.LiveStackTrace
import spp.protocol.artifact.exception.LiveStackTraceElement
import spp.protocol.artifact.log.Log
import spp.protocol.artifact.log.LogCountSummary
import spp.protocol.artifact.log.LogOrderType
import spp.protocol.artifact.log.LogResult
import spp.protocol.artifact.trace.TraceResult
import spp.protocol.instrument.*
import spp.protocol.instrument.command.CommandType
import spp.protocol.instrument.command.LiveInstrumentCommand
import spp.protocol.instrument.event.LiveBreakpointHit
import spp.protocol.instrument.event.LiveInstrumentRemoved
import spp.protocol.instrument.event.LiveLogHit
import spp.protocol.instrument.variable.LiveVariable
import spp.protocol.platform.auth.DataRedaction
import spp.protocol.platform.auth.DeveloperRole
import spp.protocol.platform.auth.RedactionType
import spp.protocol.platform.developer.SelfInfo
import spp.protocol.platform.general.Service
import spp.protocol.platform.status.ActiveInstance
import spp.protocol.view.LiveViewSubscription
import java.time.Instant

/**
 * Used for marshalling and unmarshalling protocol messages.
 * Avoids using annotations and Jackson modules to keep probe-jvm dependencies simple.
 *
 * @since 0.2.1
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
object ProtocolMarshaller {
    init {
        try {
            DatabindCodec.mapper().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            DatabindCodec.mapper().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            DatabindCodec.mapper().enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
        } catch (ignore: Throwable) {
        }
    }

    @JvmStatic
    fun serializeArtifactQualifiedName(value: ArtifactQualifiedName): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeArtifactQualifiedName(value: JsonObject): ArtifactQualifiedName {
        return value.mapTo(ArtifactQualifiedName::class.java)
    }

    @JvmStatic
    fun serializeTraceResult(value: TraceResult): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeTraceResult(value: JsonObject): TraceResult {
        return value.mapTo(TraceResult::class.java)
    }

    @JvmStatic
    fun serializeInstant(value: Instant): String {
        return value.toEpochMilli().toString()
    }

    @JvmStatic
    fun deserializeInstant(value: String): Instant {
        return Instant.ofEpochMilli(value.toLong())
    }

    @JvmStatic
    fun serializeLogCountSummary(value: LogCountSummary): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLogCountSummary(value: JsonObject): LogCountSummary {
        return value.mapTo(LogCountSummary::class.java)
    }

    @JvmStatic
    fun serializeLiveInstrument(value: LiveInstrument): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveInstrument(value: JsonObject): LiveInstrument {
        return if (value.getString("type") == "BREAKPOINT") {
            value.mapTo(LiveBreakpoint::class.java)
        } else if (value.getString("type") == "LOG") {
            value.mapTo(LiveLog::class.java)
        } else if (value.getString("type") == "METER") {
            value.mapTo(LiveMeter::class.java)
        } else if (value.getString("type") == "SPAN") {
            value.mapTo(LiveSpan::class.java)
        } else {
            throw UnsupportedOperationException("Live instrument type: " + value.getString("type"))
        }
    }

    @JvmStatic
    fun serializeLiveBreakpoint(value: LiveBreakpoint): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveBreakpoint(value: JsonObject): LiveBreakpoint {
        return value.mapTo(LiveBreakpoint::class.java)
    }

    @JvmStatic
    fun serializeLiveLog(value: LiveLog): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveLog(value: JsonObject): LiveLog {
        return value.mapTo(LiveLog::class.java)
    }

    @JvmStatic
    fun serializeLiveMeter(value: LiveMeter): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveMeter(value: JsonObject): LiveMeter {
        return value.mapTo(LiveMeter::class.java)
    }

    @JvmStatic
    fun serializeLiveSpan(value: LiveSpan): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveSpan(value: JsonObject): LiveSpan {
        return value.mapTo(LiveSpan::class.java)
    }

    @JvmStatic
    fun serializeLiveSourceLocation(value: LiveSourceLocation): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveSourceLocation(value: JsonObject): LiveSourceLocation {
        return value.mapTo(LiveSourceLocation::class.java)
    }

    @JvmStatic
    fun serializeLiveViewSubscription(value: LiveViewSubscription): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveViewSubscription(value: JsonObject): LiveViewSubscription {
        return value.mapTo(LiveViewSubscription::class.java)
    }

    @JvmStatic
    fun serializeSelfInfo(value: SelfInfo): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeSelfInfo(value: JsonObject): SelfInfo {
        return value.mapTo(SelfInfo::class.java)
    }

    @JvmStatic
    fun serializeService(value: Service): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeService(value: JsonObject): Service {
        return value.mapTo(Service::class.java)
    }

    @JvmStatic
    fun serializeActiveInstance(value: ActiveInstance): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeActiveInstance(value: JsonObject): ActiveInstance {
        return value.mapTo(ActiveInstance::class.java)
    }

    @JvmStatic
    fun serializeLiveInstrumentCommand(value: LiveInstrumentCommand): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveInstrumentCommand(value: JsonObject): LiveInstrumentCommand {
        return LiveInstrumentCommand(
            CommandType.valueOf(value.getString("commandType")),
            value.getJsonArray("instruments").list.map {
                if (it is JsonObject) {
                    deserializeLiveInstrument(it)
                } else {
                    deserializeLiveInstrument(JsonObject.mapFrom(it))
                }
            }.toSet(),
            value.getJsonArray("locations").list.map {
                if (it is JsonObject) {
                    deserializeLiveSourceLocation(it)
                } else {
                    deserializeLiveSourceLocation(JsonObject.mapFrom(it))
                }
            }.toSet()
        )
    }

    @JvmStatic
    fun serializeLiveInstrumentRemoved(value: LiveInstrumentRemoved): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveInstrumentRemoved(value: JsonObject): LiveInstrumentRemoved {
        return LiveInstrumentRemoved(
            deserializeLiveInstrument(value.getJsonObject("liveInstrument")),
            value.let {
                if (it.getValue("occurredAt") is String) {
                    Instant.parse(value.getString("occurredAt"))
                } else if (it.getValue("occurredAt") is Number) {
                    Instant.ofEpochMilli(value.getLong("occurredAt"))
                } else {
                    Instant.ofEpochSecond(
                        value.getJsonObject("occurredAt").getLong("epochSeconds"),
                        value.getJsonObject("occurredAt").getInteger("nanosecondsOfSecond").toLong()
                    )
                }
            },
            value.getJsonObject("cause")?.let { deserializeLiveStackTrace(it) }
        )
    }

    @JvmStatic
    fun serializeLiveVariable(value: LiveVariable): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveVariable(value: JsonObject): LiveVariable {
        return value.mapTo(LiveVariable::class.java)
    }

    @JvmStatic
    fun serializeLiveStackTraceElement(value: LiveStackTraceElement): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveStackTraceElement(value: JsonObject): LiveStackTraceElement {
        return value.mapTo(LiveStackTraceElement::class.java)
    }

    @JvmStatic
    fun serializeLiveStackTrace(value: LiveStackTrace): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveStackTrace(value: JsonObject): LiveStackTrace {
        return value.mapTo(LiveStackTrace::class.java)
    }

    @JvmStatic
    fun serializeLiveBreakpointHit(value: LiveBreakpointHit): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveBreakpointHit(value: JsonObject): LiveBreakpointHit {
        return LiveBreakpointHit(
            value.getString("breakpointId"),
            value.getString("traceId"),
            value.let {
                if (it.getValue("occurredAt") is String) {
                    Instant.parse(value.getString("occurredAt"))
                } else if (it.getValue("occurredAt") is Number) {
                    Instant.ofEpochMilli(value.getLong("occurredAt"))
                } else {
                    Instant.ofEpochSecond(
                        value.getJsonObject("occurredAt").getLong("epochSeconds"),
                        value.getJsonObject("occurredAt").getInteger("nanosecondsOfSecond").toLong()
                    )
                }
            },
            value.getString("serviceInstance"),
            value.getString("service"),
            deserializeLiveStackTrace(value.getJsonObject("stackTrace"))
        )
    }

    @JvmStatic
    fun serializeLogResult(value: LogResult): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLogResult(value: JsonObject): LogResult {
        return LogResult(
            value.getJsonObject("artifactQualifiedName")?.let { deserializeArtifactQualifiedName(it) },
            LogOrderType.valueOf(value.getString("orderType")),
            value.let {
                if (it.getValue("timestamp") is String) {
                    Instant.parse(value.getString("timestamp"))
                } else if (it.getValue("timestamp") is Number) {
                    Instant.ofEpochMilli(value.getLong("timestamp"))
                } else {
                    Instant.ofEpochSecond(
                        value.getJsonObject("timestamp").getLong("epochSeconds"),
                        value.getJsonObject("timestamp").getInteger("nanosecondsOfSecond").toLong()
                    )
                }
            },
            value.getJsonArray("logs").list.map {
                if (it is JsonObject) {
                    deserializeLog(it)
                } else {
                    deserializeLog(JsonObject.mapFrom(it))
                }
            },
            value.getInteger("total")
        )
    }

    @JvmStatic
    fun serializeLiveLogHit(value: LiveLogHit): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLiveLogHit(value: JsonObject): LiveLogHit {
        return LiveLogHit(
            value.getString("logId"),
            value.let {
                if (it.getValue("occurredAt") is String) {
                    Instant.parse(value.getString("occurredAt"))
                } else if (it.getValue("occurredAt") is Number) {
                    Instant.ofEpochMilli(value.getLong("occurredAt"))
                } else {
                    Instant.ofEpochSecond(
                        value.getJsonObject("occurredAt").getLong("epochSeconds"),
                        value.getJsonObject("occurredAt").getInteger("nanosecondsOfSecond").toLong()
                    )
                }
            },
            value.getString("serviceInstance"),
            value.getString("service"),
            deserializeLogResult(value.getJsonObject("logResult"))
        )
    }

    @JvmStatic
    fun serializeLog(value: Log): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeLog(value: JsonObject): Log {
        return Log(
            value.let {
                if (it.getValue("timestamp") is String) {
                    Instant.parse(value.getString("timestamp"))
                } else if (it.getValue("timestamp") is Number) {
                    Instant.ofEpochMilli(value.getLong("timestamp"))
                } else {
                    Instant.ofEpochSecond(
                        value.getJsonObject("timestamp").getLong("epochSeconds"),
                        value.getJsonObject("timestamp").getInteger("nanosecondsOfSecond").toLong()
                    )
                }
            },
            value.getString("content"),
            value.getString("level"),
            value.getString("logger"),
            value.getString("thread"),
            value.getJsonObject("exception")?.let { deserializeLiveStackTrace(it) },
            value.getJsonArray("arguments").list.map { it.toString() }
        )
    }

    @JvmStatic
    fun serializeDataRedaction(value: DataRedaction): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeDataRedaction(value: JsonObject): DataRedaction {
        return DataRedaction(
            value.getString("id"),
            RedactionType.valueOf(value.getString("type")),
            value.getString("lookup"),
            value.getString("replacement")
        )
    }

    @JvmStatic
    fun serializeDeveloperRole(value: DeveloperRole): JsonObject {
        return JsonObject(Json.encode(value))
    }

    @JvmStatic
    fun deserializeDeveloperRole(value: JsonObject): DeveloperRole {
        return DeveloperRole(
            value.getString("roleName"),
            value.getBoolean("nativeRole")
        )
    }
}
