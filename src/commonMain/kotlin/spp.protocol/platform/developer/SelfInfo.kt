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
package spp.protocol.platform.developer

import kotlinx.serialization.Serializable
import spp.protocol.platform.auth.AccessPermission
import spp.protocol.platform.auth.DeveloperRole
import spp.protocol.platform.auth.RolePermission

@Serializable
data class SelfInfo(
    val developer: Developer,
    val roles: List<DeveloperRole>,
    val permissions: List<RolePermission>,
    val access: List<AccessPermission>
)
