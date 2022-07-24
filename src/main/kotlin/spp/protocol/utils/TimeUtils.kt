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
package spp.protocol.utils

import kotlin.jvm.JvmOverloads

fun Int.toPrettyDuration(translate: (String)->String = { it }): String {
    val days = this / 86400000.0
    if (days > 1) {
        return "${days.toInt()}" + translate("dys")
    }
    val hours = this / 3600000.0
    if (hours > 1) {
        return "${hours.toInt()}" + translate("hrs")
    }
    val minutes = this / 60000.0
    if (minutes > 1) {
        return "${minutes.toInt()}" + translate("mins")
    }
    val seconds = this / 1000.0
    if (seconds > 1) {
        return "${seconds.toInt()}" + translate("secs")
    }
    return "$this" + translate("ms")
}

@JvmOverloads
fun Double.fromPerSecondToPrettyFrequency(translate: (String)->String = { it }): String {
    return when {
        this > 1000000.0 -> "${this / 1000000.0.toInt()}M/" + translate("sec")
        this > 1000.0 -> "${this / 1000.0.toInt()}K/" + translate("sec")
        this > 1.0 -> "${this.toInt()}/" + translate("sec")
        else -> "${(this * 60.0).toInt()}/" + translate.invoke("min")
    }
}

fun Long.toPrettyDuration(translate: (String)->String = { it }): String {
    val days = this / 86400000.0
    if (days > 1) {
        return "${days.toInt()}" + translate("dys")
    }
    val hours = this / 3600000.0
    if (hours > 1) {
        return "${hours.toInt()}" + translate("hrs")
    }
    val minutes = this / 60000.0
    if (minutes > 1) {
        return "${minutes.toInt()}" + translate("mins")
    }
    val seconds = this / 1000.0
    if (seconds > 1) {
        return "${seconds.toInt()}" + translate("secs")
    }
    return "$this" + translate("ms")
}
