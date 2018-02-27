public class Movie {

    private String title;
    private String year;
    private String rating;
    private String genre;

    public Movie(String title, String year, String rating, String genre){
        this.genre = genre;
        this.title = title;
        this.rating = rating;
        this.year = year;
    }

    public Movie() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYearO(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRatingO(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String toString() {

        return "Title:" + getTitle() + ",Year:" + getYear() + ",Rating:" + getRating() + ",Genre:" + getGenre();
    }
}