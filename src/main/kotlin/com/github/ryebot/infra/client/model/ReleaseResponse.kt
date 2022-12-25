package com.github.ryebot.infra.client.model

data class ReleaseResponse(
    val htmlUrl: String,
    val id: Long,
    val targetCommitish: String,
    val name: String,
    val body: String,
    val createdAt: String,
    val tagName: String? = null,
) {

    private val versions = tagName?.replace("v", "")?.split(".") ?: listOf("0", "0", "0")
    val major = versions[0].toLong()
    val minor = versions[1].toLong()
    val patch = versions[2].toLong()

    val preReleaseComment = """
        ## Release 방식
        아래와 같이 코멘트를 입력하세요.

        **예시)**
        * `!Release Version {major}.{minor}.{patch}`
        * `!Release Version 1.2.3`
        
        ## Version 추천
        아래의 버전으로 추천합니다.
        * 기존버전과 호환이 되지 않는 경우 -> `!Release Version ${major + 1}.$minor.$patch`
        * 기존버전에 신규 기능 추가된 경우 -> `!Release Version $major.${minor + 1}.$patch`
        * 기존버전에 기능/버그 수정된 경우 -> `!Release Version $major.$minor.${patch + 1}`
    """.trimIndent()
}
