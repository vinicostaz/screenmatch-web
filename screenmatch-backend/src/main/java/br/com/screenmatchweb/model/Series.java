package br.com.screenmatchweb.model;

import br.com.screenmatchweb.service.QueryMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Series {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique = true)
        private String title;
        private Integer totalSeasons;
        private Double rating;
        @Enumerated(EnumType.STRING)
        private Category genre;
        private String actors;
        private String poster;
        private String plot;

        @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<Episode> episodes = new ArrayList<>();

        public Series (SeriesData seriesData) {
            this.title = seriesData.title();
            this.totalSeasons = seriesData.totalSeasons();
            this.rating = OptionalDouble.of(Double.valueOf(seriesData.rating())).orElse(0.0);
            this.genre = Category.fromString(seriesData.genre().split(",")[0].trim());
            this.actors = seriesData.actors();
            this.poster = seriesData.poster();
            this.plot = QueryMyMemory.getTranslation(seriesData.plot()).trim();
            // this.plot = seriesData.plot();
        }

    public Series() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
            episodes.forEach(e -> e.setSeries(this));
        this.episodes = episodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return "genre = " + genre +
                ", title = '" + title + '\'' +
                ", totalSeasons = " + totalSeasons +
                ", rating = " + rating +
                ", actors = '" + actors + '\'' +
                ", poster = '" + poster + '\'' +
                ", plot = '" + plot + '\'' +
                ", episodes = " + episodes + '\'';
    }

}