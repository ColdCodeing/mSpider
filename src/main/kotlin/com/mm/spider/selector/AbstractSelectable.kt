package com.mm.spider.selector

import java.util.ArrayList

abstract class AbstractSelectable : Selectable {
    protected abstract fun getSourceTexts() : List<String>

    override fun css(selector: String): Selectable {
        return `$`(selector)
    }

    override fun css(selector: String, attrname: String): Selectable {
        return `$`(selector, attrname)
    }

    override fun regex(regex: String): Selectable {
        return selectList(RegexSelector(regex), getSourceTexts())
    }

    override fun regex(regex: String, group: Int): Selectable {
        return selectList(RegexSelector(regex, group), getSourceTexts())
    }

    override fun replace(regex: String, replacement: String): Selectable {
        return select(ReplaceSelector(regex, replacement), getSourceTexts())
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
        return select(selector, getSourceTexts())
    }
    override fun selectList(selector: Selector) : Selectable {
        return selectList(selector, getSourceTexts())
    }

    override fun get(): String? {
        if (getSourceTexts().size > 0) {
            return getSourceTexts().get(0)
        } else {
            return null
        }
    }

    override fun all(): List<String> {
        return getSourceTexts()
    }

    override fun toString(): String {
        return get().toString()
    }

    override fun match(): Boolean {
        return getSourceTexts().size > 0;
    }
}