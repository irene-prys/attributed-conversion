package demo.advertisement.conversion;

import demo.advertisement.conversion.domain.Campaign;
import demo.advertisement.conversion.utils.FileParser;
import demo.advertisement.conversion.utils.FileReaderException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AttributedConversionTest {
    @Test
    public void shouldAttributeToProspectingCampaignAndFindLastClick() throws FileReaderException {
        //given
        FileParser.ParsedData parsedData = parseFile("prospecting-campaign.json");
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
        FileParser.ParsedData parsedData = parseFile("retargeting-campaign.json");
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
        FileParser.ParsedData parsedData = parseFile("different-actions.json");
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

    private FileParser.ParsedData parseFile(String fileName) throws FileReaderException {
        String pathToFile = FileReaderTest.class.getClassLoader().getResource(fileName).getPath();
        FileParser fileReader = new FileParser();
        return fileReader.parse(pathToFile);
    }
}
