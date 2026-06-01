package org.osnormais.drive.api.infrastructure.transferchannel.gateway;

import java.util.Optional;

import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelCommandGateway;
import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelQueryGateway;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelId;
import org.osnormais.drive.api.infrastructure.transferchannel.persistence.TransferChannelJpa;
import org.osnormais.drive.api.infrastructure.transferchannel.persistence.TransferChannelJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransferChannelJpaGateway implements TransferChannelQueryGateway, TransferChannelCommandGateway {

    private final TransferChannelJpaRepository transferChannelRepository;

    public TransferChannelJpaGateway(final TransferChannelJpaRepository transferChannelRepository) {
        this.transferChannelRepository = transferChannelRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<TransferChannel> findById(final TransferChannelId id) {
        return transferChannelRepository
                .findById(id.getValue())
                .map(TransferChannelJpa::toDomain);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void create(final TransferChannel transferChannel) {
        save(transferChannel);
    }

    private void save(final TransferChannel transferChannel) {
        transferChannelRepository.save(TransferChannelJpa.fromDomain(transferChannel));
    }

}
