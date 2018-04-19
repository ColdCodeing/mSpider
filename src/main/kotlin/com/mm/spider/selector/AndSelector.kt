package com.mm.spider.selector

import java.util.ArrayList

class AndSelector : Selector {

    var selectors: List<Selector> = ArrayList<Selector>()

    constructor(selectors: List<Selector>) {
        this.selectors = selectors
    }

    constructor(selectors: Array<out Selector>) {
        this.selectors = selectors.toList<Selector>()
    }

    override fun select(html: String): String? {
        var text: String? = html
        selectors.forEach({
            if (text == null) {
                return null
            }
            text = it.select(text!!)
        })
        return text
    }

    override fun selectList(html: String): List<String>{
        var results: List<String> = ArrayList()
        var first = true
        selectors.forEach({selector ->
            if (first) {
                results = selector.selectList(html)
                first = false
            } else {
                results =  results.flatMap { selector.selectList(it) }.toList()
                if ((results as ArrayList<String>).size == 0) {
                    return results
                }
            }
        })
        return results
    }
}