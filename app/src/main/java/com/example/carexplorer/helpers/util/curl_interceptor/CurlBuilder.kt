//package com.example.carexplorer.helpers.util.curl_interceptor
//
//import okhttp3.RequestBody
//import java.nio.Buffer
//
//class CurlBuilder @JvmOverloads constructor(
//    request: Request,
//    limit: Long = -1L,
//    private val delimiter: String = " "
//) {
//    private val url: String = request.url.toString()
//    private val method: String = request.method
//    private var contentType: String? = null
//    private var body: String? = null
//    private val headers = request.headers.map { Header(it.first, it.second) }
//
//    init {
//        val body = request.body
//        if (body != null) {
//            contentType = getContentType(body)
//            this.body = getBodyAsString(body, limit)
//        }
//    }
//
//    private fun getContentType(body: RequestBody): String? {
//        val mediaType = body.contentType()
//        return mediaType?.toString()
//    }
//
//    private fun getBodyAsString(body: RequestBody, limit: Long): String {
//        return try {
//            val sink = Buffer()
//            val mediaType = body.contentType()
//            val charset = getCharset(mediaType)
//            if (limit > 0) {
//                val buffer = LimitedSink(sink, limit).buffer()
//                body.writeTo(buffer)
//                buffer.flush()
//            } else {
//                body.writeTo(sink)
//            }
//            sink.readString(charset!!)
//        } catch (e: IOException) {
//            "Error while reading body: $e"
//        }
//    }
//
//    private fun getCharset(mediaType: MediaType?): Charset? {
//        return if (mediaType != null) {
//            mediaType.charset(Charset.defaultCharset())
//        } else Charset.defaultCharset()
//    }
//
//    fun build(): String {
//        val parts: MutableList<String?> = ArrayList()
//        parts.add("curl")
//        parts.add(String.format(FORMAT_METHOD, method.toUpperCase()))
//        parts.add(String.format(FORMAT_URL, url))
//        for (header in headers) {
//            val headerPart = String.format(FORMAT_HEADER, header.name, header.value)
//            parts.add(headerPart)
//        }
//        if (contentType != null && !containsName(CONTENT_TYPE, headers)) {
//            parts.add(String.format(FORMAT_HEADER, CONTENT_TYPE, contentType))
//        }
//        if (body.isNotNullOrBlank()) {
//            parts.add(String.format(FORMAT_BODY, body))
//        }
//        return parts.joinToString(separator = delimiter)
//    }
//
//    private fun containsName(name: String, headers: List<Header>): Boolean =
//        headers.any { it.name == name }
//
//    companion object {
//        private const val FORMAT_HEADER = "-H \"%1\$s:%2\$s\""
//        private const val FORMAT_METHOD = "-X %1\$s"
//        private const val FORMAT_BODY = "-d '%1\$s'"
//        private const val FORMAT_URL = "\"%1\$s\""
//        private const val CONTENT_TYPE = "Content-Type"
//    }
//}
