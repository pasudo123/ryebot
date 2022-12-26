package com.github.ryebot.config.web.filter

import com.github.ryebot.config.web.CustomRequestServletWrapper
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * request body 를 여러번 읽을 수 있게 한다.
 */
class CustomOncePerFilterForCache : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        filterChain.doFilter(CustomRequestServletWrapper(request), response)
    }
}
