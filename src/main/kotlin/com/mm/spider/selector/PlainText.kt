package com.mm.spider.selector

import kotlin.streams.toList

open class PlainText : AbstractSelectable {

    var sourceText: List<String>

    constructor(sourceTexts: List<String>) {
        this.sourceText = sourceTexts
    }

    constructor(text: String) {
        this.sourceText = ArrayList()
        (this.sourceText as MutableList).add(text)
    }

    override fun `$`(selector: String): Selectable {
        throw UnsupportedOperationException("c can not apply to plain text")
    }

    override fun `$`(selector: String, attrname: String): Selectable {
        throw UnsupportedOperationException("c can not apply to plain text")
    }

    override fun links(): Selectable {
        throw UnsupportedOperationException("links can not apply to plain text")
    }

    override fun nodes(): List<Selectable> {
        return sourceText.stream().map { PlainText(it)}.toList()
    }

    override fun getSourceTexts(): List<String> {
        return sourceText
    }
}