package com.github.ryebot.domain.deploy.model

import com.github.ryebot.BaseTestSupport
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@BaseTestSupport
internal class DeployParamTest {

    @ParameterizedTest
    @ValueSource(strings = ["1.0.0", "1.2.3", "1.0.22", "2.1.0"])
    @DisplayName("배포파람의 사용자 코멘트와 타이틀의 버전은 서로 일치한다.")
    fun isCommentVersionNotMatchTitleVersionTest(version: String) {

        // given
        val deployParam = DeployParam(
            DeployParam.PullRequest(
                1,
                "\uD83D\uDE80 Release version $version",
                "release",
                "release"
            ),
            DeployParam.Repository(
                "owner",
                "repository"
            ),
            "!Release Version $version"
        )

        // when
        val actual = deployParam.isCommentVersionMatchTitleVersion()

        // then
        actual shouldBe true
    }

    @Test
    @DisplayName("배포파람의 사용자 코멘트와 타이틀의 버전은 서로 불일치한다.")
    fun isCommentVersionNotMatchTitleVersionTest_Failed() {

        // given
        val deployParam = DeployParam(
            DeployParam.PullRequest(
                1,
                "\uD83D\uDE80 Release version 1.2.3",
                "release",
                "release"
            ),
            DeployParam.Repository(
                "owner",
                "repository"
            ),
            "!Release Version 2.1.2"
        )

        // when
        val actual = deployParam.isCommentVersionMatchTitleVersion()

        // then
        actual shouldBe false
    }
}