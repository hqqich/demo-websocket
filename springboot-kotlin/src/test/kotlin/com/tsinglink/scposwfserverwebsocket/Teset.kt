package com.tsinglink.scposwfserverwebsocket

import org.junit.jupiter.api.Test

class Teset {

    @Test
    fun `aa`() {
        /**
         * 例如：http://qqq/book?id=1&name=烫烫烫
         */
        var url="http://qqq/book?id=1&name=烫烫烫"
        val urlValue = url.substringAfter('?', "")//获取?后的串
        /**
         * 切割字符串得到n组k-v型列表
         */
        val urlParams = urlValue.split("&")
//        val urlParams = Bundle()

        var result: HashMap<String, Any> = HashMap()

        urlParams.forEach {
            val param = it.split("=")
            if (param.size == 2) {
                result[param[0]] = param[1]
            }
        }


        println(result)

    }

}