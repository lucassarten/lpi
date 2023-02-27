package cyan.lpi.module;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

/**
 * The Date module provides utilities for working with dates.
 */
@ModuleDef(desc = "utilities for working with dates")
public class Date implements Module {
    /**
     * Convert a date from ISO 8601 to a human readable format in NZT
     * 
     * @param params
     * @return
     */
    @CommandDef(desc = "convert a date from ISO 8601 to a human readable format in NZT", params = { "<date>" })
    public static String utc(Map<String, String> params) {
        String date = params.get("0");
        // Convert to NZ time
        return Instant.parse(date)
                .atZone(ZoneId.of("Pacific/Auckland"))
                .format(
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                .withLocale(Locale.ENGLISH));
    }

    /**
     * Convert a date from one time zone to another
     * 
     * @param params
     * @return
     */
    @CommandDef(desc = "convert a date from one time zone to another", params = { "<date>", "<from>", "<to>" })
    public static String convert(Map<String, String> params) {
        String date = params.get("0");
        String from = params.get("1");
        String to = params.get("2");
        // parse from timezone to ZoneId
        if (from.equals("NZT")) {
            from = "Pacific/Auckland";
        }
        if (to.equals("NZT")) {
            to = "Pacific/Auckland";
        }
        // convert from one time zone to another
        return Instant.parse(date)
                .atZone(ZoneId.of(from))
                .withZoneSameInstant(ZoneId.of(to))
                .format(
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                .withLocale(Locale.ENGLISH));
    }

    /**
     * Get the current date and time in NZT
     * 
     * @param params
     * @return
     */
    @CommandDef(desc = "get the current date and time in NZT")
    public static String now(Map<String, String> params) {
        return Instant.now()
                .atZone(ZoneId.of("Pacific/Auckland"))
                .format(
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                .withLocale(Locale.ENGLISH));
    }

    @CommandDef(desc = "Add time to a date", params = { "<date>", "<addition>", "<unit>" })
    public static String add(Map<String, String> params) {
        String date = params.get("0");
        String addition = params.get("1");
        String unit = params.get("2");
        unit = unit.toUpperCase();
        ChronoUnit temporalUnit = ChronoUnit.valueOf(unit);
        // add addition to date
        return Instant.parse(date)
                .plus(Long.parseLong(addition), temporalUnit)
                .atZone(ZoneId.of("Pacific/Auckland"))
                .format(
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                .withLocale(Locale.ENGLISH));
    }
}
