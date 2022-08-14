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
package spp.protocol.platform.auth

data class DeveloperRole(val roleName: String, val nativeRole: Boolean) {
    companion object {
        val ROLE_MANAGER = DeveloperRole("role_manager", true)
        val ROLE_USER = DeveloperRole("role_user", true)

        fun fromString(roleName: String): DeveloperRole {
            return if (roleName.equals("role_manager", true)) {
                ROLE_MANAGER
            } else if (roleName.equals("role_user", true)) {
                ROLE_USER
            } else {
                DeveloperRole(roleName.toLowerCase().replace(' ', '_').trim(), false)
            }
        }
    }
}
