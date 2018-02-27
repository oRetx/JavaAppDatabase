import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application {

    private Stage window;
    private TableView <Movie> moviesTableView = new TableView <>();
    private TextField titleInp, yearInp, ratingInp, genreInp, searchField;
    private ObservableList <Movie> movieList = FXCollections.observableArrayList();

    public static void main(String[] args) throws IOException {

        launch( args );
    }

    public ObservableList <Movie> getMovieList() {
        movieList.add( new Movie( "Pulp Fiction", "1994", "8.9", "Crime" ) );
        movieList.add( new Movie( "Goodfellas", "1990", "8.7", "Crime" ) );
        movieList.add( new Movie( "Scarface", "1983", "8.3", "Crime" ) );
        movieList.add( new Movie( "Digimon:TheMovie", "2000", "6", "Animation" ) );

        moviesTableView.setEditable(true);

        moviesTableView.refresh();

        return movieList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane rootGrid = new GridPane();
        rootGrid.setAlignment( Pos.CENTER );
        rootGrid.setPadding( new Insets( 16 ) );
        rootGrid.setHgap( 16 );
        rootGrid.setVgap( 8 );

        primaryStage.setTitle( "Welcome!" );
        primaryStage.setWidth( 400 );
        primaryStage.setHeight( 250 );

        Button stageButton = new Button( "Enter the Movie List" );
        rootGrid.getChildren().addAll( stageButton );
        stageButton.setAlignment( Pos.CENTER );

        stageButton.setOnAction( (ActionEvent e) -> {
            try {
                stageButtonClicked();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            primaryStage.close();
        } );

        Scene scene = new Scene( rootGrid );
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    private void openFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/Drago/Desktop/MovieBaseList.txt")));
            String line;
            String[] array;

            while ((line = br.readLine()) != null){
                array = line.split(",");
                moviesTableView.getItems().add(new Movie(array[0].substring(6), (array[1].substring(5)), (array[2].substring(7)), array[3].substring(6)));
            }
            br.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void saveFile(ObservableList<Movie> movieList, File file) {
        try {
            BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));

            for(Movie mov : movieList) {
                outWriter.write(mov.toString());
                outWriter.newLine();
            }
            outWriter.close();
        } catch (IOException e) {
            Alert ioAlert = new Alert(Alert.AlertType.ERROR, "ERROR!", ButtonType.OK);
            ioAlert.setContentText("An error has occurred.");
            ioAlert.showAndWait();
            if(ioAlert.getResult() == ButtonType.OK) {
                ioAlert.close();
            }
        }
    }

    private void stageButtonClicked() throws IOException {

        HBox topMenu = new HBox();

        final Button fileButton = new Button( "Open File" );
        final Button saveButton = new Button( "Save File" );

        fileButton.setOnAction( e ->{
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(window);

            if(file != null){
                openFile();
            }
        } );

        saveButton.setOnAction( e -> {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(window);

            if(file != null){
                saveFile(movieList, file);
            }
        } );

        topMenu.getChildren().addAll( fileButton, saveButton );
        BorderPane borderPane = new BorderPane();
        borderPane.setTop( topMenu );

        Stage secondaryStage = new Stage();
        window = secondaryStage;
        StackPane primaryLayout = new StackPane();

        secondaryStage.setScene( new Scene( primaryLayout ) );
        secondaryStage.setTitle( "List of Movies" );

        TableColumn <Movie, String> titleColumn = new TableColumn <>( "Title" );
        titleColumn.setMinWidth( 200 );
        titleColumn.setCellValueFactory( new PropertyValueFactory <>( "title" ) );

        TableColumn <Movie, Integer> yearColumn = new TableColumn <>( "Year" );
        yearColumn.setMinWidth( 200 );
        yearColumn.setCellValueFactory( new PropertyValueFactory <>( "year" ) );

        TableColumn <Movie, Double> ratingColumn = new TableColumn <>( "Rating" );
        ratingColumn.setMinWidth( 200 );
        ratingColumn.setCellValueFactory( new PropertyValueFactory <>( "rating" ) );

        TableColumn <Movie, String> genreColumn = new TableColumn <>( "Genre" );
        genreColumn.setMinWidth( 200 );
        genreColumn.setCellValueFactory( new PropertyValueFactory <>( "genre" ) );

        searchField = new TextField();
        searchField.setPromptText( "Search" );
        searchField.setMaxWidth( 200 );

        titleInp = new TextField();
        titleInp.setPromptText( "Title" );
        titleInp.setMaxWidth( 500 );

        yearInp = new TextField();
        yearInp.setPromptText( "Year" );
        yearInp.setMaxWidth( 200 );

        ratingInp = new TextField();
        ratingInp.setPromptText( "Rating" );
        ratingInp.setMaxWidth( 200 );

        genreInp = new TextField();
        genreInp.setPromptText( "Genre" );
        genreInp.setMaxWidth( 200 );

        Button delButton = new Button( "Delete" );
        delButton.setOnAction( this::delButtonClicked );

        Button addButton = new Button( "Add" );
        addButton.setOnAction( this::addButtonClicked );

        BooleanBinding booleanBinding = new BooleanBinding() {
            {
                super.bind(titleInp.textProperty(),
                        yearInp.textProperty(),
                        ratingInp.textProperty(),
                        genreInp.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (titleInp.getText().isEmpty()
                        || yearInp.getText().isEmpty()
                        || ratingInp.getText().isEmpty()
                        || genreInp.getText().isEmpty());
            }
        };

        addButton.disableProperty().bind(booleanBinding);

        HBox hBox = new HBox();
        hBox.setPadding( new Insets( 10, 10, 10, 10 ) );
        hBox.setSpacing( 10 );
        hBox.getChildren().addAll( titleInp, yearInp, ratingInp, genreInp, addButton, delButton );

        moviesTableView = new TableView <>();
        moviesTableView.setItems( movieList);
        moviesTableView.getColumns().addAll( titleColumn, yearColumn, ratingColumn, genreColumn );

        VBox vBox = new VBox();
        vBox.getChildren().addAll( topMenu, searchField, moviesTableView, hBox );

        Scene scene = new Scene( vBox );
        secondaryStage.setScene( scene );
        secondaryStage.show();

        FilteredList <Movie> filteredMovies = new FilteredList <>( getMovieList(), e -> true );
        searchField.setOnKeyReleased( e -> {
            searchField.textProperty().addListener( (observableValue, oldValue, newValue) -> {
                filteredMovies.setPredicate( (Predicate <? super Movie>) movie -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (movie.getTitle().contains( newValue )) {
                        return true;
                    } else if (movie.getTitle().toLowerCase().contains( lowerCaseFilter )) {
                        return true;
                    } else if (movie.getGenre().toLowerCase().contains( lowerCaseFilter )) {
                        return true;
                    }
                    return false;
                } );
            } );
            SortedList <Movie> sortedMovies = new SortedList <Movie>( filteredMovies );
            sortedMovies.comparatorProperty().bind( moviesTableView.comparatorProperty() );
            moviesTableView.setItems( sortedMovies );
        } );
    }

    public void delButtonClicked(ActionEvent event) {
        if (!movieList.isEmpty()) {
            Alert deleteAlert = new Alert( Alert.AlertType.WARNING, "Ok", ButtonType.OK, ButtonType.CANCEL );
            Window owner = ((Node) event.getTarget()).getScene().getWindow();
            deleteAlert.setContentText( "Are you sure you want to delete this?" );
            deleteAlert.initModality( Modality.APPLICATION_MODAL );
            deleteAlert.initOwner( owner );
            deleteAlert.showAndWait();
            if (deleteAlert.getResult() == ButtonType.OK) {
                movieList.removeAll( moviesTableView.getSelectionModel().getSelectedItems() );
                moviesTableView.getSelectionModel().clearSelection();
            } else {
                deleteAlert.close();
            }
        }
    }

    private void addButtonClicked(ActionEvent event) {
        if (movieList.size() < 20) {
            Movie movie = new Movie();
            movie.setTitle( titleInp.getText() );
            movie.setGenre( genreInp.getText() );
            movie.setRatingO( ratingInp.getText()  );
            movie.setYearO(  yearInp.getText()  );

            movieList.add( movie );

            titleInp.clear();
            yearInp.clear();
            genreInp.clear();
            ratingInp.clear();
        }
    }
}