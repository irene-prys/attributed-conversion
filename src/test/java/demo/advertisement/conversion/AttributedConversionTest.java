package demo.advertisement.conversion;

import demo.advertisement.conversion.domain.AttributionWindow;
import demo.advertisement.conversion.domain.Campaign;
import demo.advertisement.conversion.domain.CampaignType;
import demo.advertisement.conversion.domain.History;
import demo.advertisement.conversion.utils.FileReader;
import demo.advertisement.conversion.utils.FileReaderException;
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
    public void shouldAttributeToRetargetingCampaign() {//retargeting-test.json
        //given
        Campaign campaign1 = new Campaign(CampaignType.PROSPECTING, 234,
                new AttributionWindow(getDate(2017, Month.JUNE, 20), getDate(2017, Month.JUNE, 24)));
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
        assertFalse(campaign1.isAttributed());
        assertTrue(campaign2.isAttributed());
    }

    @Test
    public void shouldReturnCampaignWithLatestClick() throws FileReaderException{//retargeting-test.json
        //given
        FileReader.ParsedData parsedData = parseFile("different-actions-test.json");
        AttributedConversion attributedConversion = new AttributedConversion();
        List<Campaign> campaigns = parsedData.getCampaigns();

        //when
        long campaignId = attributedConversion.attributeConversion(parsedData.getPurchaseDate(),
                parsedData.getHistories(), campaigns);
        //then
        assertEquals(345l, campaignId);
        assertTrue(campaigns.get(0).isAttributed());
        assertFalse(campaigns.get(1).isAttributed());

    }

    private FileReader.ParsedData parseFile(String fileName) throws FileReaderException{
        String pathToFile = FileReaderTest.class.getClassLoader().getResource(fileName).getPath();
        FileReader fileReader = new FileReader();
        return fileReader.readData(pathToFile);
    }

    private Date getDate(int year, Month month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
