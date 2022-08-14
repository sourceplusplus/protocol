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
