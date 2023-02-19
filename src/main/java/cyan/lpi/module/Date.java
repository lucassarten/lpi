package cyan.lpi.module;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;

@ModuleDef(desc = "utilities for working with dates")
public class Date implements Module {
    @CommandDef(desc = "<timestamp> - convert a date from ISO 8601 to a human readable format")
    public static String convert(Map<String, String> params) {
        String date = params.get("0");
        return Instant.parse(date)
                .atZone(ZoneId.of("Pacific/Auckland"))
                .format(
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                .withLocale(Locale.ENGLISH));
    }
}
