package com.seiko.net.util

import com.seiko.net.Converter
import okhttp3.RequestBody
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

open class TypeLiteral<T> {
  val type: Type
    get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun <T> Converter.convert(response: Response, type: Type): T {
  return convert(response.throwIfFatal(), type)
}

inline fun <reified T> Converter.convert(value: T): RequestBody {
  return convert(value, T::class.java)
}

/**
 * baseUrl endWith '/'
 */
fun wrapperUrl(baseUrl: String, url: String): String {
  if (url.startsWith("http")) return url
  if (url.startsWith("/")) return baseUrl + url.substring(1)
  return "$baseUrl$url"
}