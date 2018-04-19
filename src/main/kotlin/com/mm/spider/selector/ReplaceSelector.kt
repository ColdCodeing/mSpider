package com.mm.spider.selector

import java.util.regex.Pattern

class ReplaceSelector(var regexStr: String, var replacement: String) : Selector {

    var regex: Pattern

    override fun select(html: String): String {
        val matcher = regex.matcher(html)
        return matcher.replaceAll(replacement)
    }

    override fun selectList(html: String): List<String> {
        throw UnsupportedOperationException()
    }

    init {
        this.regex = Pattern.compile(regexStr)
    }
}