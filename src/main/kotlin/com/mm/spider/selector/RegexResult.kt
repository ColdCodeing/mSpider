package com.mm.spider.selector

import java.util.*

class RegexResult() {
    companion object {
        val EMPTY_RESULT = RegexResult()
    }

    var groups: Array<String>? = null

    constructor(groups: Array<String>) : this() {
        this.groups = groups
    }

    operator fun get(groupId: Int): String? {
        if(groups == null) {
            return null
        } else {
            return groups?.get(groupId)
        }
    }

    override fun toString(): String {
        return "RegexResult(groups=${Arrays.toString(groups)})"
    }
}