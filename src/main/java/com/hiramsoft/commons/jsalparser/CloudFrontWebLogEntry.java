package com.hiramsoft.commons.jsalparser;

import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * Created by ihiram on 8/27/14.
 */
public class CloudFrontWebLogEntry {

    // http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/AccessLogs.html

    // used by the builder because the CloudFront format splits date and time apart
    // even though it's way more useful to get a proper DateTime object
    // so, someone has to store the state, and it seems to make the most sense to store here
    protected String dateHolder;
    protected String timeHolder;
    private OffsetDateTime dateTime;
    private String xEdgeLocation;
    private long serverToClientBytes;
    private String clientIpAddress;
    private String clientToServerMethod;
    private String clientToServerHost;
    private String clientToServerUriStem;
    private int serverToClientStatus;
    private String clientToServerReferrer;
    private String clientToServerUserAgent;
    private String clientToServerStatus;
    private String clientToServerQueryString;
    private String clientToServerCookies;
    private String xEdgeResultType;
    private String xEdgeRequestId;
    private String xHostHeader;
    private boolean clientToServerIsHttps;
    private long clientToServerBytes;
    private long timeTakenMilliSeconds;
    private ArrayList<String> extras;

    public CloudFrontWebLogEntry() {
        extras = new ArrayList<>();
    }

    public ArrayList<String> getExtras() {
        return extras;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getxEdgeLocation() {
        return xEdgeLocation;
    }

    public void setxEdgeLocation(String xEdgeLocation) {
        this.xEdgeLocation = xEdgeLocation;
    }

    public long getServerToClientBytes() {
        return serverToClientBytes;
    }

    public void setServerToClientBytes(long serverToClientBytes) {
        this.serverToClientBytes = serverToClientBytes;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getClientToServerMethod() {
        return clientToServerMethod;
    }

    public void setClientToServerMethod(String clientToServerMethod) {
        this.clientToServerMethod = clientToServerMethod;
    }

    public String getClientToServerHost() {
        return clientToServerHost;
    }

    public void setClientToServerHost(String clientToServerHost) {
        this.clientToServerHost = clientToServerHost;
    }

    public String getClientToServerUriStem() {
        return clientToServerUriStem;
    }

    public void setClientToServerUriStem(String clientToServerUriStem) {
        this.clientToServerUriStem = clientToServerUriStem;
    }

    public int getServerToClientStatus() {
        return serverToClientStatus;
    }

    public void setServerToClientStatus(int serverToClientStatus) {
        this.serverToClientStatus = serverToClientStatus;
    }

    public String getClientToServerReferrer() {
        return clientToServerReferrer;
    }

    public void setClientToServerReferrer(String clientToServerReferrer) {
        this.clientToServerReferrer = clientToServerReferrer;
    }

    public String getClientToServerUserAgent() {
        return clientToServerUserAgent;
    }

    public void setClientToServerUserAgent(String clientToServerUserAgent) {
        this.clientToServerUserAgent = clientToServerUserAgent;
    }

    public String getClientToServerStatus() {
        return clientToServerStatus;
    }

    public void setClientToServerStatus(String clientToServerStatus) {
        this.clientToServerStatus = clientToServerStatus;
    }

    public String getClientToServerQueryString() {
        return clientToServerQueryString;
    }

    public void setClientToServerQueryString(String clientToServerQueryString) {
        this.clientToServerQueryString = clientToServerQueryString;
    }

    public String getClientToServerCookies() {
        return clientToServerCookies;
    }

    public void setClientToServerCookies(String clientToServerCookies) {
        this.clientToServerCookies = clientToServerCookies;
    }

    public String getxEdgeResultType() {
        return xEdgeResultType;
    }

    public void setxEdgeResultType(String xEdgeResultType) {
        this.xEdgeResultType = xEdgeResultType;
    }

    public String getxEdgeRequestId() {
        return xEdgeRequestId;
    }

    public void setxEdgeRequestId(String xEdgeRequestId) {
        this.xEdgeRequestId = xEdgeRequestId;
    }

    public String getxHostHeader() {
        return xHostHeader;
    }

    public void setxHostHeader(String xHostHeader) {
        this.xHostHeader = xHostHeader;
    }

    public boolean isClientToServerIsHttps() {
        return clientToServerIsHttps;
    }

    public void setClientToServerIsHttps(boolean clientToServerIsHttps) {
        this.clientToServerIsHttps = clientToServerIsHttps;
    }

    public long getClientToServerBytes() {
        return clientToServerBytes;
    }

    public void setClientToServerBytes(long clientToServerBytes) {
        this.clientToServerBytes = clientToServerBytes;
    }

    public long getTimeTakenMilliSeconds() {
        return timeTakenMilliSeconds;
    }

    public void setTimeTakenMilliSeconds(long timeTakenMilliSeconds) {
        this.timeTakenMilliSeconds = timeTakenMilliSeconds;
    }

}
