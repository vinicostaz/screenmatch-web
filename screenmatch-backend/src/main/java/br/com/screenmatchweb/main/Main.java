package br.com.screenmatchweb.main;

import br.com.screenmatchweb.model.*;
import br.com.screenmatchweb.repository.SeriesRepository;
import br.com.screenmatchweb.service.ApiConsumption;
import br.com.screenmatchweb.service.DataConvert;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final ApiConsumption consumption = new ApiConsumption();
    private final DataConvert convert = new DataConvert();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<SeriesData> seriesData = new ArrayList<>();
    private SeriesRepository repository;
    private List<Series> series = new ArrayList<>();
    private Optional<Series> searchedSeries;

    public Main(SeriesRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        var option = -1;
        while (option != 0) {
        var menu = """
               1 - Search series
               2 - Search episodes
               3 - List searched series
               4 - Search series by title
               5 - Search series by actor
               6 - Top 5 series
               7 - Search series by category
               8 - Filter series
               9 - Search episode by snippet
               10 - Search the top 5 episodes from a series
               11 - Search episodes from a date
              
               0 - Exit
               """;

        System.out.println(menu);
        option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                searchSeriesWeb();
                break;
            case 2:
                searchEpisodeBySeries();
                break;
            case 3:
                listSearchedSeries();
                break;
            case 4:
                searchSeriesByTitle();
                break;
            case 5:
                searchSeriesByActor();
                break;
            case 6:
                searchTop5Series();
                break;
            case 7:
                searchSeriesByCategory();
                break;
            case 8:
                filterSeriesBySeasonAndRating();
                break;
            case 9:
                searchEpisodeBySnippet();
                break;
            case 10:
                topEpisodesBySeries();
                break;
            case 11:
                searchEpisodesFromADate();
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid option");
        }
            }
    }

    private void searchSeriesWeb() {
        SeriesData data = getSeriesData();
        Series serie = new Series(data);
        // seriesData.add(data);
        repository.save(serie);
        System.out.println(data);
    }

    private SeriesData getSeriesData() {
        System.out.println("Enter the name of the series to search");
        var seriesName = scanner.nextLine();
        var json = consumption.getData(ADDRESS + seriesName.replace(" ", "+") + API_KEY);
        return convert.getData(json, SeriesData.class);
    }

    private void searchEpisodeBySeries(){
        listSearchedSeries();
        System.out.println("Enter the name of the series to search for episodes");
        var seriesName = scanner.nextLine();

        Optional<Series> serie = repository.findByTitleContainingIgnoreCase(seriesName);

        if(serie.isPresent()) {
            var seriesFound = serie.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= seriesFound.getTotalSeasons(); i++) {
                var json = consumption.getData(ADDRESS + seriesFound.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                SeasonData seasonData = convert.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodes().stream()
                            .map(e -> new Episode(d.number(), e)))
                    .collect(Collectors.toList());
            seriesFound.setEpisodes(episodes);
            repository.save(seriesFound);
        } else {
            System.out.println("Series not found!");
        }
    }

    private void listSearchedSeries(){
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

    private void searchSeriesByTitle() {
        System.out.println("Enter the name of the series to search for episodes");
        var seriesName = scanner.nextLine();
        searchedSeries = repository.findByTitleContainingIgnoreCase(seriesName);

        if(searchedSeries.isPresent()) {
            System.out.println("Series data: " + searchedSeries.get());
        } else {
            System.out.println("Series not found!");
        }
    }

    private void searchSeriesByActor() {
        System.out.println("Enter the actor name for the search: ");
        var actorName = scanner.nextLine();
        System.out.println("Ratings starting from which value? ");
        var rating = scanner.nextDouble();
        List<Series> seriesFound = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);
        System.out.println("Series in which " + actorName + " worked: ");
        seriesFound.forEach(s ->
                System.out.println(s.getTitle() + ", rating: " + s.getRating()));
    }

    private void searchTop5Series() {
        List<Series> topSeries = repository.findTop5ByOrderByRatingDesc();
        topSeries.forEach(s ->
                System.out.println(s.getTitle() + ", rating: " + s.getRating()));
    }

    private void searchSeriesByCategory() {
        System.out.println("Which category/genre of series would you like to search for?");
        var genreName = scanner.nextLine();
        Category category = Category.fromString(genreName);
        List<Series> seriesByCategory = repository.findByGenre(category);
        System.out.println("Series from category : " + category);
        seriesByCategory.forEach(System.out::println);
    }

    private void filterSeriesBySeasonAndRating() {
        System.out.println("Filter series up to how many seasons? ");
        var totalSeasons = scanner.nextInt();
        scanner.nextLine();
        System.out.println("With ratings starting from which value? ");
        var rating = scanner.nextDouble();
        scanner.nextLine();
        List<Series> seriesFilter = repository.seriesBySeasonAndRating(totalSeasons, rating);
        System.out.println("*** Filtered series ***");
        seriesFilter.forEach(s ->
                System.out.println(s.getTitle() + "  - rating: " + s.getRating()));
    }


    private void searchEpisodeBySnippet() {
        System.out.println("Whats the name of the episode for the search?");
        var episodeSnippet = scanner.nextLine();
        List<Episode> episodesFound = repository.episodesBySnippet(episodeSnippet);
        episodesFound.forEach(e ->
                System.out.printf("Series: %s - Season: %d - Episode %d - Title: %s\n",
                        e.getSeries().getTitle(), e.getSeason(),
                        e.getEpisodeNumber(), e.getTitle()));
    }


    private void topEpisodesBySeries() {
        searchSeriesByTitle();
        if(searchedSeries.isPresent()) {
            Series series = searchedSeries.get();
            List<Episode> topEpisodes =  repository.topEpisodesBySeries(series);
            topEpisodes.forEach(e ->
                    System.out.printf("Series: %s - Season: %d - Episode %d - Title: %s- Rating: %s\n",
                            e.getSeries().getTitle(), e.getSeason(),
                            e.getEpisodeNumber(), e.getTitle(), e.getRating()));
        }
    }


    private void searchEpisodesFromADate() {
        searchSeriesByTitle();
        if(searchedSeries.isPresent()) {
            Series series = searchedSeries.get();
            System.out.println("Enter the release year limit");
            var releaseYear = scanner.nextInt();
            scanner.nextLine();

            List<Episode> yearEpisodes = repository.episodesBySeriesAndYear(series ,releaseYear);
            yearEpisodes.forEach(System.out::println);
        }
    }

}