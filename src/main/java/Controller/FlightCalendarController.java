package Controller;

import DAO.DaoEquipage;
import Entity.Equipage;
import Entity.Vol;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class FlightCalendarController {

    @FXML
    private Label monthLabel;

    @FXML
    private Button prevMonthBtn;

    @FXML
    private Button nextMonthBtn;

    @FXML
    private GridPane daysOfWeekGrid;

    @FXML
    private GridPane calendarGrid;

    private Equipage crew;
    private YearMonth currentYearMonth;
    private ArrayList<Vol> flights;
    private Set<LocalDate> flightDateRange;

    public void initData(Equipage crew) {
        this.crew = crew;
        this.currentYearMonth = YearMonth.of(2025, 5); // Start with May 2025
        this.flights = DaoEquipage.getVolParEquipage(crew);
        populateFlightDates();
        updateCalendar();
    }

    private void populateFlightDates() {
        flightDateRange = new HashSet<>();

        for (Vol vol : flights) {
            if (vol.getDateVol() != null && vol.getDateArrivee() != null) {
                LocalDate departureDate = vol.getDateVol().toLocalDate();
                LocalDate arrivalDate = vol.getDateArrivee().toLocalDate();

                // Ensure departure is not after arrival
                if (departureDate.isAfter(arrivalDate)) {
                    LocalDate temp = departureDate;
                    departureDate = arrivalDate;
                    arrivalDate = temp;
                }

                // Add all dates in the range (inclusive)
                IntStream.rangeClosed(0, (int) (arrivalDate.toEpochDay() - departureDate.toEpochDay()))
                        .mapToObj(departureDate::plusDays)
                        .forEach(flightDateRange::add);
            }
        }

        // Debug: Print flight dates
        System.out.println("Flights for crew " + crew.getCode() + ":");
        flights.forEach(vol -> System.out.println("Vol " + vol.getCode() + ": Departure=" + vol.getDateVol() +
                ", Arrival=" + vol.getDateArrivee()));
        flightDateRange.forEach(date -> System.out.println("Flight Date Range: " + date));
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();

        // Update month label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentYearMonth.format(formatter));

        // Determine the first day of the month and the number of days
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday=0, Monday=1, ..., Saturday=6
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Populate the calendar grid
        int day = 1;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 0 && col < firstDayOfWeek) {
                    // Empty cells before the first day
                    continue;
                }
                if (day > daysInMonth) {
                    break;
                }

                LocalDate date = currentYearMonth.atDay(day);
                VBox dayBox = new VBox();
                dayBox.setMinSize(50, 50);
                dayBox.setStyle("-fx-border-color: lightgray; -fx-alignment: center;");

                Label dayLabel = new Label(String.valueOf(day));
                dayBox.getChildren().add(dayLabel);

                // Highlight if the date is within a flight range
                if (flightDateRange.contains(date)) {
                    dayBox.setStyle("-fx-background-color: #4caf50; -fx-border-color: lightgray; -fx-alignment: center;"); // Green for the entire range
                    Label statusLabel = new Label("V");
                    statusLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: white;");
                    dayBox.getChildren().add(statusLabel);
                }

                calendarGrid.add(dayBox, col, row);
                day++;
            }
        }
    }

    @FXML
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }
}