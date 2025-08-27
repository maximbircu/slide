package com.maximbircu.imageloader.engine

import java.security.MessageDigest

class ImageKey(val url: String, val tag: String? = null) {
    private val key: String by lazy { computeKey() }

    private fun computeKey(): String {
        val input = if (tag != null) "$url|$tag" else url
        return try {
            val digest = MessageDigest.getInstance("MD5")
            val hash = digest.digest(input.toByteArray())
            hash.joinToString("") { "%02x".format(it) }
        } catch (_: Exception) {
            input.hashCode().toString()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageKey) return false
        return key == other.key
    }

    override fun hashCode(): Int = key.hashCode()

    override fun toString(): String = key
}
