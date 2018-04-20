package com.mm.spider.selector

import java.util.ArrayList

abstract class AbstractSelectable : Selectable {
    abstract var sourceTexts: List<String>

    override fun css(selector: String): Selectable {
        return `$`(selector)
    }

    override fun css(selector: String, attrname: String): Selectable {
        return `$`(selector, attrname)
    }

    override fun regex(regex: String): Selectable {
        return selectList(RegexSelector(regex), sourceTexts)
    }

    override fun regex(regex: String, group: Int): Selectable {
        return selectList(RegexSelector(regex, group), sourceTexts)
    }

    override fun replace(regex: String, replacement: String): Selectable {
        return select(ReplaceSelector(regex, replacement), sourceTexts)
    }


    override fun jsonPath(jsonPath: String): Selectable {
        throw UnsupportedOperationException()
    }

    fun select(selector: Selector, strings: List<String>): Selectable {
        val lists = ArrayList<String>()
        strings.forEach( {
            selector.select(it)?.let { str -> lists.add(str) }
        })
       return PlainText(lists)
    }

    fun selectList(selector: Selector, strings: List<String>): Selectable {
        return PlainText(strings.flatMap {
            selector.selectList(it)
        }.toList())
    }
    override fun select(selector: Selector) : Selectable {
        return select(selector, sourceTexts)
    }
    override fun selectList(selector: Selector) : Selectable {
        return selectList(selector, sourceTexts)
    }

    override fun get(): String? {
        if (sourceTexts.size > 0) {
            return sourceTexts.get(0)
        } else {
            return null
        }
    }

    override fun all(): List<String> {
        return sourceTexts
    }

    override fun toString(): String {
        return get().toString()
    }

    override fun match(): Boolean {
        return sourceTexts.size > 0;
    }
}