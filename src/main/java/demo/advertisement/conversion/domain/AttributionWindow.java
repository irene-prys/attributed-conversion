package demo.advertisement.conversion.domain;

import java.util.Date;

public class AttributionWindow {
    private Date start;
    private Date end;

    public AttributionWindow(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public boolean isInRange(Date date) {
        return date.after(start) && date.before(end);
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
