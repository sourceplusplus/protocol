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
 * Useful methods for formatting artifact names.
 *
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
object ArtifactNameUtils {

    fun isArtifactQualifiedName(artifactQualifiedName: String): Boolean {
        return artifactQualifiedName.contains(".") && artifactQualifiedName.contains("(") &&
                artifactQualifiedName.contains(")")
    }

    fun getShortQualifiedClassName(qualifiedName: String?): String {
        return getQualifiedClassName(qualifiedName)!!.replace("\\B\\w+(\\.)".toRegex(), "$1")
    }

    fun getShortQualifiedFunctionName(qualifiedName: String): String {
        return getShortQualifiedClassName(qualifiedName) + "." + getShortFunctionSignature(qualifiedName)
    }

    fun getQualifiedClassName(qualifiedName: String?): String? {
        if (qualifiedName == null || qualifiedName.isEmpty()
            || !qualifiedName.contains(".") || !qualifiedName.contains("(")
        ) {
            return qualifiedName
        }
        var withoutArgs = qualifiedName.substring(0, qualifiedName.indexOf("("))
        if (withoutArgs.contains("<")) {
            withoutArgs = withoutArgs.substring(0, withoutArgs.indexOf("<"))
        }
        return withoutArgs.substring(withoutArgs.lastIndexOf("?") + 1, withoutArgs.lastIndexOf("."))
    }

    fun getClassName(qualifiedMethodName: String?): String? {
        return if (qualifiedMethodName == null
            || qualifiedMethodName.isEmpty()
            || !qualifiedMethodName.contains(".")
            || !qualifiedMethodName.contains("(")
        ) {
            qualifiedMethodName?.substringAfterLast(".")
        } else {
            val qualifiedClassName = qualifiedMethodName.substring(
                0, qualifiedMethodName.substring(
                    0, qualifiedMethodName.indexOf("(")
                ).lastIndexOf(".")
            )
            qualifiedClassName.substringAfterLast(".")
        }
    }

    fun getShortFunctionSignature(qualifiedName: String): String {
        return getFunctionSignature(qualifiedName.substringBefore("#"))
            .replace("\\B\\w+(\\.)".toRegex(), "$1")
    }

    fun getFunctionSignature(qualifiedName: String): String {
        val withoutClassName = qualifiedName.substringBefore("#")
            .replace(getQualifiedClassName(qualifiedName.substringBefore("#"))!!, "")
        return withoutClassName.substring(
            withoutClassName.substring(0, withoutClassName.indexOf("(")).lastIndexOf("?") + 2
        )
            .replace("\\(java.lang.".toRegex(), "\\(").replace("<java.lang.".toRegex(), "<")
            .replace(",java.lang.".toRegex(), ",").replace("~".toRegex(), "")
    }

    fun removePackageNames(qualifiedMethodName: String?): String? {
        if (qualifiedMethodName == null || qualifiedMethodName.isEmpty() || !qualifiedMethodName.contains(".")) {
            return qualifiedMethodName
        }
        var className = qualifiedMethodName.substring(
            0, qualifiedMethodName.substring(
                0, qualifiedMethodName.indexOf("(")
            ).lastIndexOf(".")
        )
        if (className.contains("$")) {
            className = className.substring(0, className.indexOf("$"))
        }
        val arguments = qualifiedMethodName.substring(qualifiedMethodName.indexOf("("))
        val argArray = arguments.substring(1, arguments.length - 1).split(",").toTypedArray()
        val argText = StringBuilder("(")
        for (i in argArray.indices) {
            val qualifiedArgument = argArray[i]
            var newArgText = qualifiedArgument.substring(qualifiedArgument.lastIndexOf(".") + 1)
            if (qualifiedArgument.startsWith("$className$")) {
                newArgText = qualifiedArgument.substring(qualifiedArgument.lastIndexOf("$") + 1)
            }
            argText.append(newArgText)
            if (i + 1 < argArray.size) {
                argText.append(",")
            }
        }
        argText.append(")")
        val methodNameArr =
            qualifiedMethodName.substring(0, qualifiedMethodName.indexOf("(")).split("\\.").toTypedArray()
        return if (methodNameArr.size == 1) {
            methodNameArr[0] + argText.toString()
        } else {
            methodNameArr[methodNameArr.size - 2] + "." + methodNameArr[methodNameArr.size - 1] + argText.toString()
        }
    }

    fun removePackageAndClassName(qualifiedMethodName: String?): String? {
        return if (qualifiedMethodName == null || qualifiedMethodName.isEmpty()
            || !qualifiedMethodName.contains(".") || !qualifiedMethodName.contains("(")
        ) {
            qualifiedMethodName
        } else qualifiedMethodName.substring(
            qualifiedMethodName.substring(
                0, qualifiedMethodName.indexOf("(")
            ).lastIndexOf(".") + 1
        )
    }
}
