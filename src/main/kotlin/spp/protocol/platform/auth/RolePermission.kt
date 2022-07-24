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
package spp.protocol.platform.auth

import spp.protocol.platform.auth.CommandType.*

enum class RolePermission(val manager: Boolean, val commandType: CommandType) {
    RESET(true, LIVE_SERVICE),

    //devs
    ADD_DEVELOPER(true, LIVE_SERVICE),
    REMOVE_DEVELOPER(true, LIVE_SERVICE),
    GET_DEVELOPERS(true, LIVE_SERVICE),
    REFRESH_DEVELOPER_TOKEN(true, LIVE_SERVICE),

    //roles
    ADD_ROLE(true, LIVE_SERVICE),
    REMOVE_ROLE(true, LIVE_SERVICE),
    GET_ROLES(true, LIVE_SERVICE),
    GET_DEVELOPER_ROLES(true, LIVE_SERVICE),
    ADD_DEVELOPER_ROLE(true, LIVE_SERVICE),
    REMOVE_DEVELOPER_ROLE(true, LIVE_SERVICE),

    //permissions
    GET_DEVELOPER_PERMISSIONS(true, LIVE_SERVICE),
    GET_ROLE_PERMISSIONS(true, LIVE_SERVICE),
    ADD_ROLE_PERMISSION(true, LIVE_SERVICE),
    REMOVE_ROLE_PERMISSION(true, LIVE_SERVICE),

    //instrument access
    GET_ACCESS_PERMISSIONS(true, LIVE_SERVICE),
    GET_DATA_REDACTIONS(true, LIVE_SERVICE),
    UPDATE_DATA_REDACTION(true, LIVE_SERVICE),
    ADD_ACCESS_PERMISSION(true, LIVE_SERVICE),
    REMOVE_ACCESS_PERMISSION(true, LIVE_SERVICE),

    //instruments
    ADD_LIVE_BREAKPOINT(false, LIVE_INSTRUMENT),
    ADD_LIVE_LOG(false, LIVE_INSTRUMENT),
    ADD_LIVE_METER(false, LIVE_INSTRUMENT),
    ADD_LIVE_SPAN(false, LIVE_INSTRUMENT),
    GET_LIVE_INSTRUMENTS(false, LIVE_INSTRUMENT),
    GET_LIVE_BREAKPOINTS(false, LIVE_INSTRUMENT),
    GET_LIVE_LOGS(false, LIVE_INSTRUMENT),
    GET_LIVE_METERS(false, LIVE_INSTRUMENT),
    GET_LIVE_SPANS(false, LIVE_INSTRUMENT),
    REMOVE_LIVE_INSTRUMENT(false, LIVE_INSTRUMENT),
    CLEAR_ALL_LIVE_INSTRUMENTS(false, LIVE_INSTRUMENT),

    //views
    ADD_LIVE_VIEW_SUBSCRIPTION(false, LIVE_VIEW),
    REMOVE_LIVE_VIEW_SUBSCRIPTION(false, LIVE_VIEW),
    GET_LIVE_VIEW_SUBSCRIPTIONS(false, LIVE_VIEW),

//    VIEW_OVERVIEW(false, LIVE_VIEW),
    VIEW_ACTIVITY(false, LIVE_VIEW),
    VIEW_TRACES(false, LIVE_VIEW),
    VIEW_LOGS(false, LIVE_VIEW),
    SHOW_QUICK_STATS(false, LIVE_VIEW);

    companion object {
        fun fromString(s: String): RolePermission? {
            val sb = StringBuilder()
            for (i in s.indices) {
                val c = s[i]
                if (c.isUpperCase()) {
                    if (i > 0) {
                        sb.append('_')
                    }
                    sb.append(c.lowercase())
                } else {
                    sb.append(c)
                }
            }
            return values().firstOrNull { it.name.equals(sb.toString(), true) }
        }
    }
}
