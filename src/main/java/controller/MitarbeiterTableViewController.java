package controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MitarbeiterTableViewController {

    @FXML
    private Button mitarbeiterBearbeitenButton;

    @FXML
    private Button mitarbeiterLoeschenButton;

    @FXML
    private Button mitarbeiterNeuButton;

    @FXML
    private TextField mitarbeiterSucheTextField;

    @FXML
    private TableView<?> mitarbeiterTable;

    @FXML
    private TableColumn<?, ?> nachnameColumn;

    @FXML
    private TableColumn<?, ?> ortColumn;

    @FXML
    private TableColumn<?, ?> personalnummerColumn;

    @FXML
    private TableColumn<?, ?> ressortColumn;

    @FXML
    private TableColumn<?, ?> vertragstypColumn;

    @FXML
    private TableColumn<?, ?> vornameColumn;

}

