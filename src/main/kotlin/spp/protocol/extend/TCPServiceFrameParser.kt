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
package spp.protocol.extend

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.ReplyException
import io.vertx.core.eventbus.ReplyFailure
import io.vertx.core.json.JsonObject
import io.vertx.core.net.NetSocket
import io.vertx.ext.bridge.BridgeEventType
import io.vertx.ext.eventbus.bridge.tcp.impl.protocol.FrameHelper
import org.slf4j.LoggerFactory
import spp.protocol.platform.auth.RolePermission
import spp.protocol.service.error.InstrumentAccessDenied
import spp.protocol.service.error.LiveInstrumentException
import spp.protocol.service.error.LiveInstrumentException.ErrorType
import spp.protocol.service.error.PermissionAccessDenied

class TCPServiceFrameParser(val vertx: Vertx, val socket: NetSocket) : Handler<AsyncResult<JsonObject>> {

    companion object {
        private val log = LoggerFactory.getLogger(TCPServiceFrameParser::class.java)
    }

    override fun handle(event: AsyncResult<JsonObject>) {
        if (event.failed()) {
            log.error("Failed to receive frame", event.cause())
            return
        }
        val frame = event.result()
        log.trace("Received frame: {}", frame)

        if (frame.getString("replyAddress") != null) {
            val deliveryOptions = DeliveryOptions()
            frame.getJsonObject("headers").fieldNames().forEach {
                deliveryOptions.addHeader(it, frame.getJsonObject("headers").getString(it))
            }
            vertx.eventBus().request<Any>(
                frame.getString("address"), frame.getJsonObject("body"), deliveryOptions
            ).onComplete {
                if (it.succeeded()) {
                    FrameHelper.sendFrame(
                        BridgeEventType.SEND.name.lowercase(),
                        frame.getString("replyAddress"),
                        it.result().body(),
                        socket
                    )
                } else {
                    val replyException = it.cause() as ReplyException
                    FrameHelper.writeFrame(
                        JsonObject()
                            .put("type", BridgeEventType.SEND.name.lowercase())
                            .put("address", frame.getString("replyAddress"))
                            .put("failureCode", replyException.failureCode())
                            .put("failureType", replyException.failureType().name)
                            .put("message", replyException.message),
                        socket
                    )
                }
            }
        } else if (frame.getString("address") != null) {
            val body = frame.getValue("body")
            if (body == null && frame.getString("message")?.startsWith("EventBusException:") == true) {
                handleErrorFrame(frame)
            } else if (body is JsonObject && body.getString("message")?.startsWith("EventBusException:") == true) {
                handleErrorFrame(body.put("address", frame.getString("address")))
            } else {
                vertx.eventBus().publish(frame.getString("address"), body)
            }
        } else {
            //directly thrown event bus exceptions
            throw ReplyException(ReplyFailure.ERROR, frame.getString("message"))
        }
    }

    private fun handleErrorFrame(frame: JsonObject) {
        if (frame.getString("message")?.startsWith("EventBusException:") == true) {
            val rawFailure = frame.getString("rawFailure")
            val failureCode = frame.getInteger("failureCode") ?: 500
            val error = ReplyException(
                ReplyFailure.RECIPIENT_FAILURE,
                failureCode,
                rawFailure
            )

            val causeMessage = frame.getString("message")!!
            val exceptionType = causeMessage.substringAfter("EventBusException:")
                .substringBefore("[")
            val exceptionParams = causeMessage.substringAfter("[").substringBefore("]")
            val exceptionMessage = causeMessage.substringAfter("]: ").trimEnd()
            when (exceptionType) {
                "LiveInstrumentException" -> error.initCause(
                    LiveInstrumentException(ErrorType.valueOf(exceptionParams), exceptionMessage)
                )
                "InstrumentAccessDenied" -> error.initCause(InstrumentAccessDenied(exceptionParams))
                "PermissionAccessDenied" -> error.initCause(
                    PermissionAccessDenied(RolePermission.valueOf(exceptionParams))
                )
                else -> TODO()
            }
            vertx.eventBus().publish(frame.getString("address"), error)
        } else {
            throw UnsupportedOperationException(frame.toString())
        }
    }
}
