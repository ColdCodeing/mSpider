package com.mm.spider.selector

import kotlin.streams.toList

open class PlainText : AbstractSelectable {

    override var sourceTexts: List<String>

    constructor(sourceTexts: List<String>) {
        this.sourceTexts = sourceTexts
    }

    constructor(text: String) {
        this.sourceTexts = ArrayList()
        (this.sourceTexts as MutableList).add(text)
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
        return sourceTexts.stream().map { PlainText(it)}.toList()
    }
}