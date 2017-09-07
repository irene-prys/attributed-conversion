package demo.advertisement.conversion;

import demo.advertisement.conversion.domain.Campaign;
import demo.advertisement.conversion.domain.History;
import demo.advertisement.conversion.utils.FileParser;
import demo.advertisement.conversion.utils.FileReaderException;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileReaderTest {
    private FileParser.ParsedData parsedData;

    @Before
    public void setUp() throws FileReaderException {
        String pathToFile = FileReaderTest.class.getClassLoader().getResource("file-to-parse.json").getPath();
        FileParser fileReader = new FileParser();
        this.parsedData = fileReader.parse(pathToFile);
    }

    @Test
    public void shouldParseFile() {
        assertEquals("2017-06-25", formatDate(parsedData.getPurchaseDate()));
        assertEquals(2, parsedData.getHistories().size());
        assertEquals(2, parsedData.getCampaigns().size());
    }

    @Test
    public void shouldParseCampaigns() {
        List<Campaign> campaigns = parsedData.getCampaigns();

        Campaign campaign1 = campaigns.get(0);
        assertEquals(234l, campaign1.getCampaignId());
        assertEquals("prospecting", campaign1.getType().toString().toLowerCase());
        assertEquals("2017-06-20", formatDate(campaign1.getAttributionWindow().getStart()));
        assertEquals("2017-08-01", formatDate(campaign1.getAttributionWindow().getEnd()));
        assertFalse(campaign1.isAttributed());

        Campaign campaign2 = campaigns.get(1);
        assertEquals(345l, campaign2.getCampaignId());
        assertEquals("retargeting", campaign2.getType().toString().toLowerCase());
        assertEquals("2017-06-20", formatDate(campaign2.getAttributionWindow().getStart()));
        assertEquals("2017-08-01", formatDate(campaign2.getAttributionWindow().getEnd()));
        assertFalse(campaign2.isAttributed());
    }

    @Test
    public void shouldParseHistories() {
        List<History> histories = parsedData.getHistories();

        History history1 = histories.get(0);
        assertEquals("click", history1.getAction());
        assertEquals("2017-06-26", formatDate(history1.getDate()));
        assertEquals(234l, history1.getCampaignId());

        History history2 = histories.get(1);
        assertEquals("click", history2.getAction());
        assertEquals("2017-06-24", formatDate(history2.getDate()));
        assertEquals(345l, history2.getCampaignId());
    }

    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }
}
