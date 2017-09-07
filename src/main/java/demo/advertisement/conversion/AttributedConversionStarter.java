package demo.advertisement.conversion;

import demo.advertisement.conversion.utils.FileReader;
import demo.advertisement.conversion.utils.FileReaderException;

import java.io.File;

public class AttributedConversionStarter {
    private final static String FILE_NAME = "data.json";

    public static void main(String[] args) {
        FileReader jsonConverter = new FileReader();
        try {
            FileReader.ParsedData data = jsonConverter.readData(getPathToFile());
            AttributedConversion attributedConversion = new AttributedConversion();
            long campaignId = attributedConversion.attributeConversion(data.getPurchaseDate(), data.getHistories(), data.getCampaigns());
            System.out.println("--->>>>>> campaign id is " + campaignId);
        } catch (FileReaderException e) {
            System.out.println("something went wrong during the file reading");
            e.printStackTrace();
        }
    }

    private static String getPathToFile() {
        String jarFullPath = FileReader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return jarFullPath + File.separator + FILE_NAME;
    }
}
