package br.com.screenmatchweb.repository;

import br.com.screenmatchweb.model.Category;
import br.com.screenmatchweb.model.Episode;
import br.com.screenmatchweb.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByTitleContainingIgnoreCase(String seriesName);

    List<Series> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, Double rating);

    List<Series> findTop5ByOrderByRatingDesc();

    List<Series> findByGenre(Category category);

    List<Series> findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(int totalSeasons, double rating);

    @Query("SELECT s FROM Series s WHERE s.totalSeasons <= :totalSeasons AND s.rating >= :rating")
    List<Series> seriesBySeasonAndRating(int totalSeasons, double rating);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE e.title ILIKE %:episodeSnippet%")
    List<Episode> episodesBySnippet(String episodeSnippet);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series ORDER BY e.rating DESC LIMIT 5")
    List<Episode> topEpisodesBySeries(Series series);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series AND YEAR(e.releaseDate) >= :releaseYear")
    List<Episode> episodesBySeriesAndYear(Series series, int releaseYear);

    @Query("SELECT s FROM Series s JOIN s.episodes e GROUP BY s ORDER BY MAX(e.releaseDate) DESC LIMIT 5")
    List<Series> recentReleases();

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s.id = :id AND e.season = :season")
    List<Episode> getEpisodesPerSeason(Long id, Long season);
}
