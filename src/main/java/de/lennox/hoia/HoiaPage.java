package de.lennox.hoia;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HoiaPage {
  @GetMapping("/")
  private String indexPage(Model model) {
    LocalDate lastUpdate = LocalDate.of(2021, 7, 25);
    LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.of("CET"));
    model.addAttribute("fullDate", fancyDifferenceBetween(lastUpdate, today));
    model.addAttribute("days", daysDifferenceBetween(lastUpdate, today));
    // Set the index view
    return "index";
  }

  /**
   * Returns a fancy string for the time which has passed between two local dates
   *
   * @param lastUpdate The last update time
   * @param today The time today
   * @return The formatted fancy string
   */
  private String fancyDifferenceBetween(LocalDate lastUpdate, LocalDate today) {
    // Get the difference through a period
    Period difference = Period.between(lastUpdate, today);
    int years = difference.getYears();
    int months = difference.getMonths();
    int days = difference.getDays();
    // Check if the year style has to be applied;
    boolean yearStyle = years > 0;
    if (yearStyle) {
      String style = "%d year%s, %d month%s and %d day%s";
      return String.format(
          style,
          years,
          pluralCharacter(years),
          months,
          pluralCharacter(months),
          days,
          pluralCharacter(days));
    } else {
      String style = "%d month%s, %d week%s and %d day%s";
      // Change the days to the support weeks
      days %= 7;
      // Extra calculation for weeks as the Period class does not supply them
      int weeks = (int) (ChronoUnit.WEEKS.between(lastUpdate, today) % 4.34524);
      return String.format(
          style,
          months,
          pluralCharacter(months),
          weeks,
          pluralCharacter(weeks),
          days,
          pluralCharacter(days));
    }
  }

  /**
   * Returns a count of days which have passed between two supplied LocalDates
   *
   * @param lastUpdate The last update date
   * @param today The time today
   * @return The delta of days
   */
  private int daysDifferenceBetween(LocalDate lastUpdate, LocalDate today) {
    return (int) ChronoUnit.DAYS.between(lastUpdate, today);
  }

  /**
   * Returns a 's' to indicate a plural when the number is bigger than one or equal to zero
   *
   * @param number The number
   * @return The plural character
   */
  private String pluralCharacter(int number) {
    return number > 1 || number == 0 ? "s" : "";
  }
}
