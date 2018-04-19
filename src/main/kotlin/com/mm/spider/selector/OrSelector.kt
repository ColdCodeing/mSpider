package com.mm.spider.selector

import java.awt.SystemColor.text
import java.util.ArrayList



class OrSelector : Selector{
    var selectors: List<Selector> = ArrayList<Selector>()

    constructor(selectors: Array<out Selector>) {
        this.selectors = selectors.toList<Selector>() as ArrayList<Selector>
    }

    constructor(selectors: List<Selector>){
        this.selectors = selectors as ArrayList<Selector>
    }

    override fun select(html: String): String? {
        for (selector in selectors) {
            return selector.select(html)
        }
        return null
    }

    override fun selectList(html: String): List<String> {
        return selectors.flatMap { selector -> selector.selectList(html) }.toList()
    }

}