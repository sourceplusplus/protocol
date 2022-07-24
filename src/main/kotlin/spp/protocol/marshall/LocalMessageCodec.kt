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

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import java.util.*

/**
 * Used to transmit protocol messages.
 *
 * @since 0.1.0
 */
class LocalMessageCodec<T> : MessageCodec<T, T> {
    override fun encodeToWire(buffer: Buffer, o: T): Unit =
        throw UnsupportedOperationException("Not supported yet.")

    override fun decodeFromWire(pos: Int, buffer: Buffer): T =
        throw UnsupportedOperationException("Not supported yet.")

    override fun transform(o: T): T = o
    override fun name(): String = UUID.randomUUID().toString()
    override fun systemCodecID(): Byte = -1
}
