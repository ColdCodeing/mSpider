package com.mm.spider.selector

class RegexResult() {
    companion object {
        val EMPTY_RESULT = RegexResult()
    }

    var groups: Array<String> = Array(0, {""})

    constructor(groups: Array<String>) : this() {
        this.groups = groups
    }

    operator fun get(groupId: Int): String {
        return groups.get(groupId)
    }

}