package com.mm.spider.selector

import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import java.util.ArrayList




open class HtmlNode : AbstractSelectable {

    var elements: List<Element> = ArrayList<Element> ()

    constructor() : super() {

    }

    constructor(elements: List<Element>) : super() {
        this.elements = elements
    }

    fun checkElementAndConvert(elementIterator: MutableListIterator<Element>): Element {
        val element = elementIterator.next()
        if (element !is Document) {
            val root = Document(element.ownerDocument().baseUri())
            val clone = element.clone()
            root.appendChild(clone)
            elementIterator.set(root)
            return root
        }
        return element
    }

    fun selectElements(elementSelector: BaseElementSelector): Selectable {
        val elementIterator = elements.listIterator()
        if (!elementSelector.hasAttribute()) {
            val resultElements = ArrayList<Element>()
            while (elementIterator.hasNext()) {
                val element = checkElementAndConvert(elementIterator as MutableListIterator<Element>)
                val selectElements = elementSelector.selectElements(element)
                selectElements.let { resultElements.addAll(it) }
            }
            return HtmlNode(resultElements)
        } else {
            val resultStrings = ArrayList<String>()
            while (elementIterator.hasNext()) {
                val element = checkElementAndConvert(elementIterator as MutableListIterator<Element>)
                resultStrings.addAll(elementSelector.selectList(element))
            }
            return PlainText(resultStrings)
        }
    }

    override fun `$`(selector: String): Selectable {
        return selectElements(Selectors.`$`(selector))
    }

    override fun `$`(selector: String, attrname: String): Selectable {
        return selectElements(Selectors.`$`(selector, attrname))
    }

    override fun getSourceTexts(): List<String> {
        val sourceTexts = ArrayList<String>(elements.size)
        for (element in elements) {
            sourceTexts.add(element.toString())
        }
        return sourceTexts
    }

    override fun links(): Selectable {
        return selectElements(LinksSelector())
    }

    override fun nodes(): List<Selectable> {
        val selectables = ArrayList<Selectable>()
        for (element in elements) {
            val childElements = ArrayList<Element>(1)
            childElements.add(element)
            selectables.add(HtmlNode(childElements))
        }
        return selectables
    }
}