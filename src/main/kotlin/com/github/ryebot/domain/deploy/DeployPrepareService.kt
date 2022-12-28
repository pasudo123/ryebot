package com.github.ryebot.domain.deploy

import com.github.ryebot.domain.deploy.detail.DeployCiService
import com.github.ryebot.domain.deploy.detail.DeployPrepareCommentService
import com.github.ryebot.domain.deploy.detail.DeployPrepareContentService
import com.github.ryebot.domain.deploy.model.DeployPrepareParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeployPrepareService(
    private val deployPrepareContentService: DeployPrepareContentService,
    private val deployPrepareCommentService: DeployPrepareCommentService,
    private val deployCiService: DeployCiService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun prepareIfReleaseBranch(deployPrepareParam: DeployPrepareParam) = runBlocking(Dispatchers.IO) {
        if (deployPrepareParam.isBaseBranchRelease().not()) {
            log.warn("해당 브랜치[${deployPrepareParam.baseBranch}]는 릴리즈 준비를 할 수 없습니다.")
            return@runBlocking
        }

        val deployCheckParam = deployCiService.progressDeployCheck(deployPrepareParam)

        listOf(
            async { deployPrepareContentService.updateTitleAndContentOnRelease(deployPrepareParam) },
            async { deployPrepareCommentService.prepareReleaseVersionComment(deployPrepareParam) }
        ).awaitAll()

        deployCheckParam?.run {
            deployCiService.completedDeployCheck(deployPrepareParam, deployCheckParam)
        }
    }
}
