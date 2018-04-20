package com.mm.spider.selector

class Json : PlainText {
    constructor(strings: List<String>) : super(strings) {
    }

    constructor(string: String) : super(string) {
    }

    override fun jsonPath(jsonPath: String): Selectable {
        return selectList(JsonPathSelector(jsonPath), sourceTexts)
    }
}