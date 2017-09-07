package demo.advertisement.conversion;

import demo.advertisement.conversion.utils.FileReader;
import demo.advertisement.conversion.utils.FileReaderException;

public class AttributedConversionStarter {

    public static void main(String[] args) {
        FileReader jsonConverter = new FileReader();
        try {
            FileReader.ParsedData data = jsonConverter.readData();
            AttributedConversion attributedConversion = new AttributedConversion();
            long campaignId = attributedConversion.attributeConversion(data.getPurchaseDate(), data.getHistories(), data.getCampaigns());
            System.out.println("--->>>>>> campaign id is " + campaignId);
        } catch (FileReaderException e) {
            System.out.println("something went wrong during the file reading");
            e.printStackTrace();
        }
    }
}
