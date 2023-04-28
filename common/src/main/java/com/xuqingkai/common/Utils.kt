package com.xuqingkai.common

class Utils {
    companion object{
        fun timestamp():Int{
            return (System.currentTimeMillis()/1000).toInt()
        }
        fun dateTime(pattern: String?): String {
            val simpleDateFormat = java.text.SimpleDateFormat(pattern)
            return simpleDateFormat.format(java.util.Date())
        }
        fun md5(content: String): String {
            val hash = java.security.MessageDigest.getInstance("MD5").digest(content.toByteArray())
            val hex = StringBuilder(hash.size * 2)
            for (b in hash) {
                var str = Integer.toHexString(b.toInt())
                if (b < 0x10) {
                    str = "0$str"
                }
                hex.append(str.substring(str.length -2))
            }
            return hex.toString()
        }
        fun isMobile(mobile:Any?):Boolean{
            return if(mobile == null){
                false;
            }else mobile.toString().length == 11
        }
    }
}