package org.osnormais.drive.api.application.usecase.file.retrieve.list;

import java.util.UUID;

import org.osnormais.drive.api.domain.pagination.SearchQuery;

public record ListFileInput(SearchQuery query, UUID actorId) {

}
