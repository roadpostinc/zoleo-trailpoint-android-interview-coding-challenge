package com.rp.trailpoint.data

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

fun stubHttpClient(
    vararg responses: Pair<String, String>,
    responseCode: Int = 200,
): OkHttpClient {
    val byPath = responses.toMap()
    val interceptor = Interceptor { chain ->
        val request = chain.request()
        val body = byPath[request.url.encodedPath]
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(if (body == null) 404 else responseCode)
            .message(if (body == null) "Not Found" else "OK")
            .body(
                (body ?: """{"error":"Not found"}""")
                    .toResponseBody("application/json".toMediaType()),
            )
            .build()
    }
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}
