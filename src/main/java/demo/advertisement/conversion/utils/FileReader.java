package demo.advertisement.conversion.utils;

import demo.advertisement.conversion.domain.AttributionWindow;
import demo.advertisement.conversion.domain.Campaign;
import demo.advertisement.conversion.domain.CampaignType;
import demo.advertisement.conversion.domain.History;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileReader {

    public ParsedData readData(String pathToFile) throws FileReaderException {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new java.io.FileReader(pathToFile));
            JSONObject jsonObject = (JSONObject) obj;

            String purchaseDate = (String) jsonObject.get("purchase_date");
            List histories = parseHistories((JSONArray) jsonObject.get("history"));
            List campaigns = parseCampaigns((JSONArray) jsonObject.get("campaign_info"));

            return new ParsedData(convertDate(purchaseDate), histories, campaigns);

        } catch (IOException | ParseException | java.text.ParseException e) {
            throw new FileReaderException(e);
        }
    }

    private List<Campaign> parseCampaigns(JSONArray campaignsJsonArray) throws java.text.ParseException {
        List<Campaign> campaigns = new ArrayList<>();
        Iterator<JSONObject> iterator = campaignsJsonArray.iterator();
        while (iterator.hasNext()) {
            campaigns.add(parseCampaign(iterator.next()));
        }

        return campaigns;
    }

    private Campaign parseCampaign(JSONObject campaignsJsonObject) throws java.text.ParseException {
        String campaignId = (String) campaignsJsonObject.get("campaign_id");
        String campaignType = (String) campaignsJsonObject.get("campaign_type");
        String start = (String) campaignsJsonObject.get("start");
        String end = (String) campaignsJsonObject.get("end");

        return new Campaign(CampaignType.valueOf(campaignType.toUpperCase()), Long.parseLong(campaignId), new AttributionWindow(convertDate(start), convertDate(end)));
    }

    private List<History> parseHistories(JSONArray historiesJsonArray) throws java.text.ParseException {
        List<History> histories = new ArrayList<>();
        Iterator<JSONObject> iterator = historiesJsonArray.iterator();
        while (iterator.hasNext()) {
            histories.add(parseHistory(iterator.next()));
        }

        return histories;
    }

    private History parseHistory(JSONObject historyJsonObject) throws java.text.ParseException {
        String action = (String) historyJsonObject.get("action");
        String date = (String) historyJsonObject.get("date");
        String campaignId = (String) historyJsonObject.get("campaign_id");

        return new History(action, convertDate(date), Long.parseLong(campaignId));
    }

    private Date convertDate(String date) throws java.text.ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.parse(date);
    }

    public final static class ParsedData {
        private Date purchaseDate;
        private List<History> histories;
        private List<Campaign> campaigns;

        public ParsedData(Date purchaseDate, List<History> histories, List<Campaign> campaigns) {
            this.purchaseDate = purchaseDate;
            this.histories = histories;
            this.campaigns = campaigns;
        }

        public Date getPurchaseDate() {
            return purchaseDate;
        }

        public List<History> getHistories() {
            return histories;
        }

        public List<Campaign> getCampaigns() {
            return campaigns;
        }
    }
}
