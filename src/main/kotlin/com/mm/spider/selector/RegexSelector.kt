package com.mm.spider.selector

import java.util.regex.Pattern
import java.util.ArrayList

class RegexSelector : Selector {

    lateinit var regexStr: String
    lateinit var regex: Pattern
    var group: Int

    constructor(regexStr: String, group: Int) {
        this.regexStr = regexStr
        this.group = group
    }

    constructor(regexStr: String) {
        compileRegex(regexStr)
        if (regex.matcher("").groupCount() == 0) {
            this.group = 0
        } else {
            this.group = 1
        }
    }

    private fun compileRegex(regexStr: String) {
        this.regex = Pattern.compile(regexStr, 34)
        this.regexStr = regexStr
    }

    override fun selectList(html: String): List<String> {
        val strings = ArrayList<String>()
        val results = selectGroupList(html)
        for (result in results) {
            strings.add(result[group])
        }
        return strings
    }

    override fun select(html: String): String {
        return selectGroup(html)[group]
    }

    fun selectGroup(text: String) : RegexResult {
        val matcher = regex.matcher(text)
        if (matcher.find()) {
            val groups = Array<String>(matcher.groupCount() - 1, {""})
            for (i in groups.indices) {
                groups.set(i, matcher.group(i))
            }
            return RegexResult(groups)
        }
        return RegexResult.EMPTY_RESULT
    }

    fun selectGroupList(text: String) : List<RegexResult> {
        val matcher = regex.matcher(text)
        val resultList = ArrayList<RegexResult>()
        if (matcher.find()) {
            val groups = Array<String>(matcher.groupCount() - 1, {""})
            for (i in groups.indices) {
                groups.set(i, matcher.group(i))
            }
            resultList.add(RegexResult(groups))
        }
        return resultList
    }
}