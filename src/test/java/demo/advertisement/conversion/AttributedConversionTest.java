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
    public void shouldAttributeToProspectingCampaign() throws FileReaderException {
        //given
        FileReader.ParsedData parsedData = parseFile("prospecting-campaign.json");
        List<Campaign> campaigns = parsedData.getCampaigns();
        AttributedConversion attributedConversion = new AttributedConversion();

        //when
        long campaignId = attributedConversion.attributeConversion(parsedData.getPurchaseDate(), parsedData.getHistories(), campaigns);

        //then
        assertEquals(345l, campaignId);
        assertTrue(campaigns.get(0).isAttributed());
        assertFalse(campaigns.get(1).isAttributed());
    }

    @Test
    public void shouldAttributeToRetargetingCampaign() throws FileReaderException {
        //given
        FileReader.ParsedData parsedData = parseFile("retargeting-campaign.json");
        List<Campaign> campaigns = parsedData.getCampaigns();
        AttributedConversion attributedConversion = new AttributedConversion();

        //when
        long campaignId = attributedConversion.attributeConversion(parsedData.getPurchaseDate(), parsedData.getHistories(), campaigns);

        //then
        assertEquals(345l, campaignId);
        assertFalse(campaigns.get(0).isAttributed());
        assertTrue(campaigns.get(1).isAttributed());
    }

    @Test
    public void shouldReturnCampaignWithLatestClick() throws FileReaderException {
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

    private FileReader.ParsedData parseFile(String fileName) throws FileReaderException {
        String pathToFile = FileReaderTest.class.getClassLoader().getResource(fileName).getPath();
        FileReader fileReader = new FileReader();
        return fileReader.readData(pathToFile);
    }
}
