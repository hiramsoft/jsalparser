package com.hiramsoft.commons.jsalparser;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ihiram on 8/27/14.
 *
 * Because of the header line, we have to process each line according to the simple schema defined. One of the fastest
 * ways I know to write this is by using a builder pattern.
 *
 * This class is used only by the ANTLR grammar.
 */
class CloudFrontWebLogBuilder {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
    private static final Map<String, IValueBuilder> VALUE_BUILDERS = new HashMap<>();
    private static final IValueBuilder ExtraBuilder = (value, entry) -> entry.getExtras().add(value);

    static {
        VALUE_BUILDERS.put("date", (value, entry) -> entry.dateHolder = value);
        VALUE_BUILDERS.put("time", (value, entry) -> entry.timeHolder = value);
        VALUE_BUILDERS.put("x-edge-location", (value, entry) -> entry.setxEdgeLocation(value));
        VALUE_BUILDERS.put("sc-bytes", (value, entry) -> entry.setServerToClientBytes(Long.parseLong(value)));
        VALUE_BUILDERS.put("c-ip", (value, entry) -> entry.setClientIpAddress(value));
        VALUE_BUILDERS.put("cs-method", (value, entry) -> entry.setClientToServerMethod(value));
        VALUE_BUILDERS.put("cs(Host)", (value, entry) -> entry.setClientToServerHost(value));
        VALUE_BUILDERS.put("cs-uri-stem", (value, entry) -> entry.setClientToServerUriStem(value));
        VALUE_BUILDERS.put("cs-uri-query", (value, entry) -> entry.setClientToServerQueryString(value));
        VALUE_BUILDERS.put("sc-status", (value, entry) -> entry.setServerToClientStatus(Integer.parseInt(value)));
        VALUE_BUILDERS.put("cs(Referer)", (value, entry) -> entry.setClientToServerReferrer(value));
        VALUE_BUILDERS.put("cs(User-Agent)", (value, entry) -> entry.setClientToServerUserAgent(value));
        VALUE_BUILDERS.put("cs(Cookie)", (value, entry) -> entry.setClientToServerCookies(value));
        VALUE_BUILDERS.put("x-edge-result-type", (value, entry) -> entry.setxEdgeResultType(value));
        VALUE_BUILDERS.put("x-edge-request-id", (value, entry) -> entry.setxEdgeRequestId(value));
        VALUE_BUILDERS.put("x-host-header", (value, entry) -> entry.setxHostHeader(value));
        VALUE_BUILDERS.put("cs-protocol", (value, entry) -> entry.setClientToServerIsHttps(value.equals("https")));
        VALUE_BUILDERS.put("cs-bytes", (value, entry) -> entry.setClientToServerBytes(Long.parseLong(value)));
        VALUE_BUILDERS.put("time-taken", (value, entry) -> {
            // Every other time unit is in millis, so this one should be, too.
            Double partialSeconds = Double.parseDouble(value);
            entry.setTimeTakenMilliSeconds((long) (1000 * partialSeconds));
        });
    }

    private ArrayList<String> headers;
    private ArrayList<IValueBuilder> builders;
    private int position;
    private CloudFrontWebLogEntry entry;

    public CloudFrontWebLogBuilder() {
        headers = new ArrayList<>();
        builders = new ArrayList<>();
        position = 0;
    }

    public void acceptVersion(String version) {
        // No-op since there is only one version
    }

    public void reset() {
        headers.clear();
        builders.clear();
        position = 0;
        entry = null;
    }

    public void acceptHeader(String header) {
        headers.add(header);
        builders.add(VALUE_BUILDERS.getOrDefault(header, ExtraBuilder));
    }

    public void acceptValue(String value) {
        if (entry == null) {
            entry = new CloudFrontWebLogEntry();
            position = 0;
        }
        if (position < builders.size()) {
            builders.get(position).accept(value, entry);
        } else {
            ExtraBuilder.accept(value, entry);
        }
        position++;
    }

    public void skipValue() {
        position++;
    }

    public CloudFrontWebLogEntry buildEntry() {

        if (entry.dateHolder == null && entry.timeHolder == null) {
            // do nothing since no date or time information was provided
        } else if (entry.dateHolder != null && entry.timeHolder == null) {
            // we have only a date and assume midnight (in UTC time)
            String dateTimeString = entry.dateHolder + " 00:00:00" + " +0000";
            OffsetDateTime dt = OffsetDateTime.parse(dateTimeString, formatter);
            entry.setDateTime(dt);
        } else if (entry.dateHolder != null && entry.timeHolder != null) {
            // we have both a date and a time (in UTC time)
            String dateTimeString = entry.dateHolder + " " + entry.timeHolder + " +0000";
            OffsetDateTime dt = OffsetDateTime.parse(dateTimeString, formatter);
            entry.setDateTime(dt);
        } else if (entry.dateHolder == null && entry.timeHolder != null) {
            // do nothing, we need a date at a minimum
        }

        CloudFrontWebLogEntry retEntry = entry;
        entry = null;
        position = 0;

        return retEntry;
    }
}
