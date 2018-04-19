package com.mm.spider.selector

class Selectors {
    companion object {
        fun regex(regex: String) : RegexSelector {
            return RegexSelector(regex)
        }

        fun regex(regex: String, group: Int) : RegexSelector {
            return RegexSelector(regex, group)
        }

        fun `$`(expr: String): CssSelector {
            return CssSelector(expr)
        }

        fun `$`(expr: String, attrName: String): CssSelector {
            return CssSelector(expr, attrName)
        }

        fun and(vararg selectors: Selector): AndSelector {
            return AndSelector(selectors)
        }

        fun or(vararg selectors: Selector): OrSelector {
            return OrSelector(selectors)
        }
    }
}