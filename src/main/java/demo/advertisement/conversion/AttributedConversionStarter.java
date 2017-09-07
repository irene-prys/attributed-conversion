package demo.advertisement.conversion;

import demo.advertisement.conversion.utils.FileParser;
import demo.advertisement.conversion.utils.FileReaderException;

import java.io.File;

public class AttributedConversionStarter {
    private final static String FILE_NAME = "data.json";

    public static void main(String[] args) {
        FileParser jsonConverter = new FileParser();
        try {
            FileParser.ParsedData data = jsonConverter.parse(getPathToFile());
            AttributedConversion attributedConversion = new AttributedConversion();
            long campaignId = attributedConversion.attributeConversion(data.getPurchaseDate(), data.getHistories(), data.getCampaigns());
            System.out.println("--->>>>>> campaign id is " + campaignId);
        } catch (FileReaderException e) {
            System.out.println("something went wrong during the file reading");
            e.printStackTrace();
        }
    }

    private static String getPathToFile() {
        String jarFullPath = FileParser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return jarFullPath + File.separator + FILE_NAME;
    }
}
