package com.mm.spider.selector

import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

class CssSelector : BaseElementSelector {

    var selectorText: String
    var attrName: String? = null

    constructor(selectorText: String, attrName: String) {
        this.selectorText = selectorText
        this.attrName = attrName
    }

    constructor(selectorText: String) {
        this.selectorText = selectorText
    }

    fun getValue(element: Element) : String {
        if (attrName == null) {
            return element.outerHtml();
        } else if ("innerHtml".equals(attrName, true)) {
            return element.html();
        } else if ("text".equals(attrName, true)) {
            return getText(element);
        } else if ("allText".equals(attrName, true)) {
            return element.text();
        } else {
            return element.attr(attrName);
        }
    }

    fun getText(element: Element) : String {
        val accum = StringBuilder()
        for (node in element.childNodes()) {
            if (node is TextNode) {
                val textNode = node as TextNode
                accum.append(textNode.text())
            }
        }
        return accum.toString()
    }

    override fun select(element: Element): String? {
        val elements = selectElements(element)
        return elements.get(0).let { getValue(it) }
    }

    override fun selectList(elment: Element): List<String> {
        val elements = selectElements(elment)
        return elements.map { getValue(it) }.toList()
    }

    override fun selectElement(element: Element): Element? {
        return element.select(selectorText).get(0)
    }

    override fun selectElements(element: Element): List<Element> {
        return element.select(selectorText)
    }

    override fun hasAttribute(): Boolean {
        return attrName != null
    }

}