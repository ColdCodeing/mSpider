package com.mm.spider.utils

import org.jsoup.Jsoup
import java.nio.charset.Charset

fun detectCharset(contentType: String, contentBytes: ByteArray): String? {
    var charset: String?
    // charset
    // 1、encoding in downloader header Content-Type
    charset = getCharset(contentType)
    if (contentType.isNotEmpty() && !charset.isNullOrEmpty()) {
        return charset
    }
    // use default charset to decode first time
    val defaultCharset = Charset.defaultCharset()
    val content = String(contentBytes, defaultCharset)
    // 2、charset in meta
    if (content.isNotEmpty()) {
        val document = Jsoup.parse(content)
        val links = document.select("meta")
        for (link in links) {
            // 2.1、html4.01 <meta downloader-equiv="Content-Type" content="text/html; charset=UTF-8" />
            var metaContent = link.attr("content")
            val metaCharset = link.attr("charset")
            if (metaContent.indexOf("charset") != -1) {
                metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length)
                charset = metaContent.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                break
            } else if (metaCharset.isNotEmpty()) {
                charset = metaCharset
                break
            }// 2.2、html5 <meta charset="UTF-8" />
        }
    }
    return charset
}