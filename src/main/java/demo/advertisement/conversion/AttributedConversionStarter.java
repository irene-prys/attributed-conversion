package demo.advertisement.conversion;

import demo.advertisement.conversion.domain.Campaign;
import demo.advertisement.conversion.utils.FileParser;
import demo.advertisement.conversion.utils.FileReaderException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

public class AttributedConversionStarter {
    private final static String FILE_NAME = "data.json";
    private final static String ATTRIBUTED_CAMPAIGN_MSG = "Campaign that was attributed to: id = {0}, type = {1}";

    public static void main(String[] args) {
        FileParser jsonConverter = new FileParser();
        try {
            FileParser.ParsedData data = jsonConverter.parse(getPathToFile(args.length > 0 ? args[0] : FILE_NAME));
            AttributedConversion attributedConversion = new AttributedConversion();
            List<Campaign> campaigns = data.getCampaigns();
            long campaignId = attributedConversion.attributeConversion(data.getPurchaseDate(), data.getHistories(), campaigns);
            System.out.println("--->>>>>> campaign id is " + campaignId);

            Optional<Campaign> attributedCampaign = campaigns.stream().filter((campaign -> campaign.isAttributed())).findAny();
            if (attributedCampaign.isPresent()) {
                System.out.println(MessageFormat.format(ATTRIBUTED_CAMPAIGN_MSG,
                        attributedCampaign.get().getCampaignId(),
                        attributedCampaign.get().getType()));
            } else {
                System.out.println("No campaign was attributed to");
            }
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
