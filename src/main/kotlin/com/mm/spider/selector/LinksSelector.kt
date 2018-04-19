package com.mm.spider.selector

import org.jsoup.nodes.Element
import java.util.ArrayList



class LinksSelector() : BaseElementSelector() {

    override fun select(element: Element): String {
        throw UnsupportedOperationException()
    }

    override fun selectList(element: Element): List<String> {
        val elements = element.select("a")
        val links = ArrayList<String>(elements.size)
        for (element in elements) {
            if (element.baseUri().isNotBlank()) {
                links.add(element.attr("abs:href"))
            } else {
                links.add(element.attr("href"))
            }
        }
        return links
    }

    override fun selectElement(element: Element): Element {
        throw UnsupportedOperationException()
    }

    override fun selectElements(element: Element): List<Element> {
        throw UnsupportedOperationException()
    }

    override fun hasAttribute(): Boolean {
        return true
    }
}