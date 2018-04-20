package com.mm.spider.selector

import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import java.util.ArrayList




open class HtmlNode : AbstractSelectable {
    override var sourceTexts: List<String>
        get() {
            val sourceTexts = ArrayList<String>(elements.size)
            for (element in elements) {
                sourceTexts.add(element.toString())
            }
            return sourceTexts
        }
        set(value) {}

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
        val elementIterator = getElement().listIterator()
        if (!elementSelector.hasAttribute()) {
            val resultElements = ArrayList<Element>()
            while (elementIterator.hasNext()) {
                val element = checkElementAndConvert(elementIterator as MutableListIterator<Element>)
                resultElements.addAll(elementSelector.selectElements(element))
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

    open fun getElement(): List<Element> {
        return this.elements
    }
}