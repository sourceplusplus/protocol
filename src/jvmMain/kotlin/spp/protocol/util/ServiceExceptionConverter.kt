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
package spp.protocol.util

import io.vertx.serviceproxy.ServiceException
import spp.protocol.service.error.LiveInstrumentException

object ServiceExceptionConverter {

    fun fromEventBusException(exception: String): ServiceException {
        return if (exception.startsWith("EventBusException")) {
            var exceptionType = exception.substringAfter("EventBusException:")
            exceptionType = exceptionType.substringBefore("[")
            var exceptionParams = exception.substringAfter("[")
            exceptionParams = exceptionParams.substringBefore("]")
            val exceptionMessage = exception.substringAfter("]: ").trim { it <= ' ' }
            if (LiveInstrumentException::class.java.simpleName == exceptionType) {
                LiveInstrumentException(
                    LiveInstrumentException.ErrorType.valueOf(exceptionParams),
                    exceptionMessage
                ).toEventBusException()
            } else {
                throw UnsupportedOperationException(exceptionType)
            }
        } else {
            throw IllegalArgumentException(exception)
        }
    }
}
