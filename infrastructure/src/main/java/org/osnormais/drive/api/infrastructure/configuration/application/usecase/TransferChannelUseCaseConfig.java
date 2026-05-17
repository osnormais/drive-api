package org.osnormais.drive.api.infrastructure.configuration.application.usecase;

import org.osnormais.drive.api.application.gateway.entitlement.plan.PlanQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelCommandGateway;
import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelUseCase;
import org.osnormais.drive.api.application.usecase.transferchannel.create.DefaultCreateTransferChannelUseCase;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.DefaultGetTransferChannelUseCase;
import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferChannelUseCaseConfig {

    private static final Long DEFAULT_MAX_BANDWIDTH_BYTES_PER_SECOND = 512 * 1024L; // 512 KB/s
    private static final Long DEFAULT_MAX_DURATION_SECONDS = 5L * 60L; // 5 minutes

    private final FileQueryGateway fileQueryGateway;
    private final UserQueryGateway userQueryGateway;
    private final PlanQueryGateway planQueryGateway;
    private final TransferChannelCommandGateway transferChannelCommandGateway;
    private final TransferChannelQueryGateway transferChannelQueryGateway;

    public TransferChannelUseCaseConfig(
            FileQueryGateway fileQueryGateway,
            UserQueryGateway userQueryGateway,
            PlanQueryGateway planQueryGateway,
            TransferChannelCommandGateway transferChannelCommandGateway,
            TransferChannelQueryGateway transferChannelQueryGateway) {
        this.fileQueryGateway = fileQueryGateway;
        this.userQueryGateway = userQueryGateway;
        this.planQueryGateway = planQueryGateway;
        this.transferChannelCommandGateway = transferChannelCommandGateway;
        this.transferChannelQueryGateway = transferChannelQueryGateway;
    }

    @Bean
    CreateTransferChannelUseCase createTransferChannelUseCase() {
        return new DefaultCreateTransferChannelUseCase(
                DEFAULT_MAX_BANDWIDTH_BYTES_PER_SECOND,
                DEFAULT_MAX_DURATION_SECONDS,
                userQueryGateway,
                fileQueryGateway,
                planQueryGateway,
                transferChannelCommandGateway);
    }

    @Bean
    GetTransferChannelUseCase getTransferChannelUseCase() {
        return new DefaultGetTransferChannelUseCase(userQueryGateway, transferChannelQueryGateway);
    }

}
