package demo.advertisement.conversion.domain;

import java.util.Date;

public class Campaign {
    private long id;
    private CampaignType type;
    private AttributionWindow attributionWindow;
    private boolean attributed;

    public Campaign() {
    }

    public Campaign(long campaignId, CampaignType type, AttributionWindow window) {
        this.id = campaignId;
        this.type = type;
        this.attributionWindow = window;
    }

    public CampaignType getType() {
        return type;
    }

    public void setType(CampaignType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
