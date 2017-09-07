package demo.advertisement.conversion;

import demo.advertisement.conversion.utils.FileParser;
import demo.advertisement.conversion.utils.FileReaderException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AttributedConversionStarter {
    private final static String FILE_NAME = "data.json";

    public static void main(String[] args) {
        FileParser jsonConverter = new FileParser();
        try {
            FileParser.ParsedData data = jsonConverter.parse(getPathToFile(args.length > 0 ? args[0] : FILE_NAME));
            AttributedConversion attributedConversion = new AttributedConversion();
            long campaignId = attributedConversion.attributeConversion(data.getPurchaseDate(), data.getHistories(), data.getCampaigns());
            System.out.println("--->>>>>> campaign id is " + campaignId);
        } catch (FileReaderException e) {
            System.out.println("something went wrong during the file reading");
            e.printStackTrace();
        }
    }

    private static String getPathToFile(String fileName) {
        String jarFullPath = FileParser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        Path jarPath = Paths.get(jarFullPath);
        String jarDirPath = jarPath.getParent().toString();
        return jarDirPath + File.separator + fileName;
    }
}
