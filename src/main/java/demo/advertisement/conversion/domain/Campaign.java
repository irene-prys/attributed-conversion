package demo.advertisement.conversion.domain;

import java.util.Date;

public class Campaign {
    private CampaignType type;
    private long campaignId;
    private AttributionWindow attributionWindow;
    private boolean attributed;// todo: think it over

    public Campaign() {
    }

    public Campaign(CampaignType type, long campaignId, AttributionWindow window) {
        this.type = type;
        this.campaignId = campaignId;
        this.attributionWindow = window;
    }

    public CampaignType getType() {
        return type;
    }

    public void setType(CampaignType type) {
        this.type = type;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public AttributionWindow getAttributionWindow() {
        return attributionWindow;
    }

    public void setAttributionWindow(AttributionWindow attributionWindow) {
        this.attributionWindow = attributionWindow;
    }

    public boolean isActive(Date eventDate) {
        return attributionWindow.isInRange(eventDate);
    }

    public void attribute() {
        attributed = true;
    }

    public boolean isAttributed() {
        return attributed;
    }
}
