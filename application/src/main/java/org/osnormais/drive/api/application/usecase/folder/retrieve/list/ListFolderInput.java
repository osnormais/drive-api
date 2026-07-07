package org.osnormais.drive.api.application.usecase.folder.retrieve.list;

import java.util.UUID;

import org.osnormais.drive.api.domain.pagination.SearchQuery;

public record ListFolderInput(SearchQuery query, UUID actorId) {

}
