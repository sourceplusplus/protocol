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
package spp.protocol.service

import io.vertx.codegen.annotations.GenIgnore
import io.vertx.codegen.annotations.ProxyGen
import io.vertx.codegen.annotations.VertxGen
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import spp.protocol.SourceServices
import spp.protocol.platform.developer.SelfInfo
import spp.protocol.platform.general.Service
import spp.protocol.platform.status.ActiveInstance

/**
 * todo: description.
 *
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
@ProxyGen
@VertxGen
interface LiveService {

    @GenIgnore
    companion object {
        @JvmStatic
        fun createProxy(vertx: Vertx, authToken: String? = null): LiveService {
            val deliveryOptions = DeliveryOptions().apply {
                authToken?.let { addHeader("auth-token", it) }
            }
            return LiveServiceVertxEBProxy(vertx, SourceServices.Utilize.LIVE_SERVICE, deliveryOptions)
        }
    }

    fun getClients(): Future<JsonObject>
    fun getStats(): Future<JsonObject>
    fun getSelf(): Future<SelfInfo>
    fun getServices(): Future<List<Service>>
    fun getActiveProbes(): Future<List<ActiveInstance>>
}
