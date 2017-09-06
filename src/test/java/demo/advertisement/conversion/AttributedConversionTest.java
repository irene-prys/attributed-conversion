package demo.advertisement.conversion;

import demo.advertisement.conversion.domain.AttributionWindow;
import demo.advertisement.conversion.domain.Campaign;
import demo.advertisement.conversion.domain.CampaignType;
import demo.advertisement.conversion.domain.History;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributedConversionTest {
    @Test
    public void shouldAttributeToProspectingCampaign() {
        //given
        Campaign campaign1 = new Campaign(CampaignType.PROSPECTING, 234,
                new AttributionWindow(getDate(2017, Month.JUNE, 20), getDate(2017, Month.AUGUST, 01)));
        Campaign campaign2 = new Campaign(CampaignType.RETARGETING, 345,
                new AttributionWindow(getDate(2017, Month.JUNE, 20), getDate(2017, Month.AUGUST, 01)));
        List<Campaign> campaigns = Arrays.asList(campaign1, campaign2);

        History history1 = new History("click", getDate(2017, Month.JUNE, 26), 234);
        History history2 = new History("click", getDate(2017, Month.JUNE, 24), 345);
        List<History> histories = Arrays.asList(history1, history2);

        AttributedConversion attributedConversion = new AttributedConversion();

        //when
        long campaignId = attributedConversion.attributeConversion(getDate(2017, Month.JUNE, 25), histories, campaigns);

        //then
        assertEquals(345l, campaignId);
        assertTrue(campaign1.isAttributed());
        assertFalse(campaign2.isAttributed());
    }

    @Test
    public void shouldAttributeToRetargetingCampaign() {

    }

    private Date getDate(int year, Month month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
