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
package spp.protocol.platform

object ProcessorAddress {
    const val REMOTE_REGISTERED = "spp.processor.status.remote-registered"
    const val LIVE_INSTRUMENT_APPLIED = "spp.processor.status.live-instrument-applied"
    const val LIVE_INSTRUMENT_REMOVED = "spp.processor.status.live-instrument-removed"

    const val SET_LOG_PUBLISH_RATE_LIMIT = "spp.processor.setting.log-publish-rate-limit"
}
