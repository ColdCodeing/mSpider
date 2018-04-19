package com.mm.spider.selector

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

class Html : HtmlNode {
    var document: Document

    constructor(text: String, url: String) : super() {
        this.document = Jsoup.parse(text, url)
    }

    constructor(text: String) : super() {
        this.document = Jsoup.parse(text)
    }

    constructor(document: Document) : super() {
        this.document = document
    }

    override fun getElement(): List<Element> {
        return Collections.singletonList(document)
    }

    fun selectDocument(selector: Selector): String? {
        if (selector is ElementSelector) {
            val elementSelector = selector as ElementSelector
            return elementSelector.select(document)
        } else {
            return selector.select(getSourceTexts().get(0))
        }
    }

    fun selectDocumentForList(selector: Selector): List<String> {
        if (selector is ElementSelector) {
            val elementSelector = selector as ElementSelector
            return elementSelector.selectList(document)
        } else {
            return selector.selectList(getSourceTexts().get(0))
        }
    }

    override fun toString(): String {
        return "Html(document=$document)"
    }
}