package com.mm.spider.utils

class HttpConstant {
    companion object {
        class Method {
            companion object {
                val GET = "GET"
                val HEAD = "HEAD"
                val POST = "POST"
                val PUT = "PUT"
                val DELETE = "DELETE"
                val TRACE = "TRACE"
                val CONNECT = "CONNECT"
            }
        }

        class StatusCode {
            companion object {
                val CODE_200 = 200
            }
        }

        class Header {
            companion object {
                val REFERER = "Referer"
                val USER_AGENT = "User-Agent"
            }
        }
    }

}