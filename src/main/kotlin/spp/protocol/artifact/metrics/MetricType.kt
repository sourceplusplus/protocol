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
package spp.protocol.artifact.metrics

/**
 * todo: description.
 *
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
enum class MetricType {
    Throughput_Average,
    ResponseTime_Average,
    ServiceLevelAgreement_Average,
    ResponseTime_99Percentile,
    ResponseTime_95Percentile,
    ResponseTime_90Percentile,
    ResponseTime_75Percentile,
    ResponseTime_50Percentile;

    val responseTimePercentile: Boolean
        get() = this == ResponseTime_99Percentile
                || this == ResponseTime_95Percentile
                || this == ResponseTime_90Percentile
                || this == ResponseTime_75Percentile
                || this == ResponseTime_50Percentile

    val simpleName: String
        get() = when (this) {
            Throughput_Average -> "Throughput"
            ResponseTime_Average -> "Response"
            ServiceLevelAgreement_Average -> "SLA"
            ResponseTime_99Percentile -> "Resp(99%)"
            ResponseTime_95Percentile -> "Resp(95%)"
            ResponseTime_90Percentile -> "Resp(90%)"
            ResponseTime_75Percentile -> "Resp(75%)"
            ResponseTime_50Percentile -> "Resp(50%)"
        }

    companion object {
        //todo: remove
        fun realValueOf(name: String): MetricType {
            return (values().find { it.name == name }
                ?: when (name) {
                    "endpoint_cpm" -> Throughput_Average
                    "endpoint_avg", "endpoint_resp_time" -> ResponseTime_Average
                    "endpoint_sla" -> ServiceLevelAgreement_Average
                    "endpoint_percentile" -> ResponseTime_99Percentile
                    else -> throw UnsupportedOperationException(name)
                })
        }
    }
}
