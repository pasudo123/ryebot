package com.github.ryebot.domain.deploy

import com.github.ryebot.domain.deploy.detail.DeployMergeService
import com.github.ryebot.domain.deploy.detail.DeployPolicyService
import com.github.ryebot.domain.deploy.detail.DeployReleaseService
import com.github.ryebot.domain.deploy.model.DeployBranchParam
import org.springframework.stereotype.Service

@Service
class DeployBranchService(
    private val deployPolicyService: DeployPolicyService,
    private val deployMergeService: DeployMergeService,
    private val deployReleaseService: DeployReleaseService,
) {

    suspend fun releaseIfPossibleOrNot(deployBranchParam: DeployBranchParam) {
        if (deployPolicyService.possible(deployBranchParam).not()) {
            return
        }

        if (deployMergeService.merge(deployBranchParam).not()) {
            return
        }

        deployReleaseService.release(deployBranchParam)
    }
}
