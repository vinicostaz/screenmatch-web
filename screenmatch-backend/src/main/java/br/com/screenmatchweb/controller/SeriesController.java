package br.com.screenmatchweb.controller;

import br.com.screenmatchweb.dto.EpisodeDTO;
import br.com.screenmatchweb.dto.SeriesDTO;
import br.com.screenmatchweb.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @GetMapping
    public List<SeriesDTO> getSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/top5")
    public List<SeriesDTO> getTop5Series() {
        return seriesService.getTop5Series();
    }

    @GetMapping("/releases")
    public List<SeriesDTO> getReleases() {
        return seriesService.getReleases();

    }

    @GetMapping("/{id}")
    public SeriesDTO getSeriesById(@PathVariable Long id) {
        return seriesService.getById(id);
    }

    @GetMapping("/{id}/seasons/all")
    public List<EpisodeDTO> getAllSeasons(@PathVariable Long id) {
        return seriesService.getAllSeasons(id);
    }

    @GetMapping("/{id}/seasons/{season}")
    public List<EpisodeDTO> getSeasonByNumber(@PathVariable Long id, @PathVariable Long season) {
        return seriesService.getSeasonByNumber(id, season);
    }

    @GetMapping("/category/{genreName}")
    public List<SeriesDTO> getSeriesByCategory(@PathVariable String genreName) {
        return seriesService.getSeriesByCategory(genreName);
    }

}
