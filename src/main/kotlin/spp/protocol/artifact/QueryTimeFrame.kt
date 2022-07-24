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
package spp.protocol.artifact

/**
 * todo: description.
 *
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
enum class QueryTimeFrame(val minutes: Int, val id: String) {
    //todo: support custom time frames
    LAST_MINUTE(1, "1_minute"),
    LAST_5_MINUTES(5, "5_minutes"),
    LAST_15_MINUTES(15, "15_minutes"),
    LAST_30_MINUTES(30, "30_minutes"),
    LAST_HOUR(60, "hour"),
    LAST_3_HOURS(60 * 3, "3_hours"); //todo: id = enum name

    val description = id.toUpperCase().replace("_", " ")

    companion object {
        fun valueOf(minutes: Int): QueryTimeFrame {
            for (timeFrame in values()) {
                if (timeFrame.minutes == minutes) {
                    return timeFrame
                }
            }
            throw IllegalArgumentException("No time frame for minutes: $minutes")
        }
    }
}
