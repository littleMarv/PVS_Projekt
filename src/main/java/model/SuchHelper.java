package model;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SuchHelper {

     /**
     * Verknüpft eine ObservableList und ein Suchfeld live mit einer Tabelle.
     *
     * @param suchFeld Das TextField für die Eingabe
     * @param tabelle Die TableView, die gefiltert wird
     * @param originalDaten Deine ungefilterte ObservableList
     */
    public static <T> void verknuepfe(TextField suchFeld, TableView<T> tabelle, ObservableList<T> originalDaten) {
        // 1. Die FilteredList direkt an deine ObservableList hängen
        FilteredList<T> filteredData = new FilteredList<>(originalDaten, p -> true);

        // 2. Filter-Logik bei Tastendruck
        suchFeld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }

                String suchBegriff = newValue.toLowerCase().trim();

                // Schneller Spalten-Scan
                for (TableColumn<T, ?> spalte : tabelle.getColumns()) {
                    if (spalte.getCellData(item) != null) {
                        String zellenText = spalte.getCellData(item).toString().toLowerCase();
                        if (zellenText.contains(suchBegriff)) {
                            return true;
                        }
                    }
                }
                return false;
            });
        });

        // 3. Die gefilterte Ansicht in die Tabelle setzen
        tabelle.setItems(filteredData);
    }
}



