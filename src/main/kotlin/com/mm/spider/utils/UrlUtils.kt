package com.mm.spider.utils

import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.util.regex.Pattern

fun canonicalizeUrl(url: String, refer: String): String {
    var url = url
    val base: URL
    try {
        try {
            base = URL(refer)
        } catch (e: MalformedURLException) {
            // the base is unsuitable, but the attribute may be abs on its own, so try that
            val abs = URL(refer)
            return abs.toExternalForm()
        }

        // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (url.startsWith("?"))
            url = base.getPath() + url
        val abs = URL(base, url)
        return abs.toExternalForm()
    } catch (e: MalformedURLException) {
        return ""
    }
}

fun encodeIllegalCharacterInUrl(url: String): String {
    return url.replace(" ", "%20")
}

fun fixIllegalCharacterInUrl(url: String): String {
    //TODO more charator support
    return url.replace(" ", "%20").replace("#+".toRegex(), "#")
}

fun getHost(url: String): String {
    var host = url
    val i = url.indexOf("/", 3)
    if (i > 0) {
        host = url.substring(0, i)
    }
    return host
}

private val patternForProtocal = Pattern.compile("[\\w]+://")

fun removeProtocol(url: String): String {
    return patternForProtocal.matcher(url).replaceAll("")
}

fun getDomain(url: String): String {
    var domain = removeProtocol(url)
    val i = domain.indexOf("/", 1)
    if (i > 0) {
        domain = domain.substring(0, i)
    }
    return removePort(domain)
}

fun removePort(domain: String): String {
    val portIndex = domain.indexOf(":")
    return if (portIndex != -1) {
        domain.substring(0, portIndex)
    } else {
        domain
    }
}

val patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)", Pattern.CASE_INSENSITIVE)

fun getCharset(contentType: String): String? {
    val matcher = patternForCharset.matcher(contentType)
    if (matcher.find()) {
        val charset = matcher.group(1)
        if (Charset.isSupported(charset)) {
            return charset
        }
    }
    return null
}
