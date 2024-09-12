package br.com.screenmatchweb.model;

public enum Category {
    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    TERROR("Terror", "Terror");

    private String imdbCategory;
    private String portugueseCategory;

    private Category(String imdbCategory, String portugueseCategory) {
        this.imdbCategory = imdbCategory;
        this.portugueseCategory = portugueseCategory;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.imdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category found for the provided string. " + text);
    }

    public static Category fromPortuguese(String text) {
        for (Category category : Category.values()) {
            if (category.portugueseCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category found for the provided string. " + text);
    }
}
