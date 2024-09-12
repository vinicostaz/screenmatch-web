package br.com.screenmatchweb.service;

import br.com.screenmatchweb.dto.EpisodeDTO;
import br.com.screenmatchweb.dto.SeriesDTO;
import br.com.screenmatchweb.model.Category;
import br.com.screenmatchweb.model.Series;
import br.com.screenmatchweb.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository repository;
    @Autowired
    private SeriesRepository seriesRepository;

    public List<SeriesDTO> getAllSeries() {
        return convertData(repository.findAll());
    }

    public List<SeriesDTO> getTop5Series() {
        return convertData(repository.findTop5ByOrderByRatingDesc());
    }

    public List<SeriesDTO> getReleases() {
        return convertData(repository.recentReleases());
    }

    private List<SeriesDTO> convertData(List<Series> series) {
        return series.stream()
                .map(s -> new SeriesDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getPoster(), s.getPlot()))
                .collect(Collectors.toList());
    }

    public SeriesDTO getById(Long id) {
        Optional<Series> series = repository.findById(id);

        if(series.isPresent()) {
            Series s = series.get();
            return new SeriesDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getPoster(), s.getPlot());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Series> series = repository.findById(id);

        if(series.isPresent()) {
            Series s = series.get();
            return s.getEpisodes().stream()
                    .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeNumber(), e.getTitle()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodeDTO> getSeasonByNumber(Long id, Long season) {
        return repository.getEpisodesPerSeason(id, season)
                .stream()
                .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeNumber(), e.getTitle()))
                .collect(Collectors.toList());
    }

    public List<SeriesDTO> getSeriesByCategory(String genreName) {
        Category category = Category.fromString(genreName);
        return convertData(repository.findByGenre(category));
    }
}
