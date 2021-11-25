//package com.example.carexplorer.helpers.util.curl_interceptor
//
//import okhttp3.Interceptor
//import okhttp3.Response
//import java.io.IOException
//
///**
// * Interceptor responsible for printing curl logs
// * @param log output of logging
// * @param limit limit maximal bytes logged, if negative - non limited
// * @param delimiter string delimiter
// */
//class CurlInterceptor @JvmOverloads constructor(
//    private val limit: Long = DEFAULT_LIMIT,
//    private val delimiter: String = DEFAULT_DELIMITER,
//    private val log: (String) -> Unit
//) : Interceptor {
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val copy = request.newBuilder().build()
//        val curl = CurlBuilder(copy, limit, delimiter).build()
//        log(curl)
//        return chain.proceed(request)
//    }
//
//    companion object {
//        private const val DEFAULT_LIMIT = 1024L * 1024L
//        private const val DEFAULT_DELIMITER = " "
//    }
//}
