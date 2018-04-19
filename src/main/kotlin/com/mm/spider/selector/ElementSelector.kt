package com.mm.spider.selector

import org.jsoup.nodes.Element

interface ElementSelector {
    fun select(element: Element) : String?
    fun selectList(elment: Element) : List<String>
}