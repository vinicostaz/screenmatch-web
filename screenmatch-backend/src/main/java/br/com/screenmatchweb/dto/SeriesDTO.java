package br.com.screenmatchweb.dto;

import br.com.screenmatchweb.model.Category;

public record SeriesDTO(Long id, String title, Integer totalSeasons, Double rating, Category genre, String actors, String poster, String plot) {

}
