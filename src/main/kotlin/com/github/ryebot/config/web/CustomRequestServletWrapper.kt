package com.github.ryebot.config.web

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * request.body 의 내용을 두번 읽기 위함
 * https://sejoung.github.io/2020/06/2020-06-01-ContentCachingRequestWrapper/#HttpMessageNotReadableException-%EC%97%90%EB%9F%AC
 */
class CustomRequestServletWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private var encoding: Charset
    private var rawString: String

    init {
        val currentEncoding = request.characterEncoding ?: StandardCharsets.UTF_8.toString()
        this.encoding = Charset.forName(currentEncoding)
        this.rawString = String(request.inputStream.readAllBytes(), encoding)
    }

    override fun getInputStream(): ServletInputStream {
        return CachedServletInputStream(this.rawString.toByteArray())
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream, this.encoding))
    }

    private class CachedServletInputStream(private val contents: ByteArray) : ServletInputStream() {

        private val buffer = ByteArrayInputStream(contents)

        override fun read(): Int {
            return buffer.read()
        }

        override fun isFinished(): Boolean {
            return this.buffer.available() == 0
        }

        override fun isReady(): Boolean {
            return true
        }

        override fun setReadListener(listener: ReadListener?) {
            throw UnsupportedOperationException("지원하지 않는 기능입니다.")
        }
    }
}
