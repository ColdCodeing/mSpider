package com.mm.spider.selector

import java.awt.SystemColor.text


class Json : PlainText {
    constructor(strings: List<String>) : super(strings) {
    }

    constructor(string: String) : super(string) {
    }

    override fun jsonPath(jsonPath: String): Selectable {
        return selectList(JsonPathSelector(jsonPath), sourceText)
    }
}