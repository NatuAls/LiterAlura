package com.aluracursos.LiterAlura.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
        String title,
        List<AuthorData> authors,
        List<String> languages,
        @JsonAlias("download_count")
        Integer downloadCount
) {
}