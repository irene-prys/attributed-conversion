package demo.advertisement.conversion.domain;

import java.util.Date;

public class History {
    private String action;
    private Date date;
    private long campaignId;

    public History() {
    }

    public History(String action, Date date, long campaignId) {
        this.action = action;
        this.date = date;
        this.campaignId = campaignId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }
}
