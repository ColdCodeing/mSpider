package com.mm.spider.selector

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

abstract class BaseElementSelector : Selector, ElementSelector{
    override fun select(html: String): String? {
        return select(Jsoup.parse(html))
    }

    override fun selectList(html: String): List<String> {
        return selectList(Jsoup.parse(html))
    }

    fun selectElement(text: String) : Element? {
        return selectElement(Jsoup.parse(text))
    }

    fun selectElements(text: String) : List<Element> {
        return selectElements(Jsoup.parse(text))
    }

    abstract fun selectElement(element: Element) : Element?
    abstract fun selectElements(element: Element) : List<Element>

    abstract fun hasAttribute() : Boolean
}