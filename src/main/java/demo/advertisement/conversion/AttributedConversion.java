package demo.advertisement.conversion;

import demo.advertisement.conversion.domain.Campaign;
import demo.advertisement.conversion.domain.CampaignType;
import demo.advertisement.conversion.domain.History;

import java.util.*;
import java.util.stream.Collectors;

public class AttributedConversion {
    public final static long NOT_FOUND = -1;
    private final static String ACTION_TO_ATTRIBUTE = "click";

    /**
     * @param purchaseDate
     * @param histories    List of user's actions, with a date and campaign ids.
     * @param campaigns
     * @return Campaign id of a click action that most recently occured right before the purchase date.
     */
    public Long attributeConversion(Date purchaseDate, List<History> histories, List<Campaign> campaigns) {
        Optional<Campaign> prospectingCampaign = findActiveProspectingCampaign(purchaseDate, campaigns);
        if (prospectingCampaign.isPresent()) {
            return prospectingCampaign.get().getCampaignId();
        }

        Optional<Long> campaignWithLastClickAction = findCampaignWithLastClickAction(histories, campaigns);
        return campaignWithLastClickAction.isPresent() ? campaignWithLastClickAction.get() : NOT_FOUND;
    }

    private Optional<Campaign> findActiveProspectingCampaign(Date purchaseDate, List<Campaign> campaigns) {

        List<Campaign> prospectingCampaigns = campaigns.stream().
                filter(campaign -> campaign.getType() == CampaignType.PROSPECTING
                        && campaign.isActive(purchaseDate)).collect(Collectors.toList());
        Campaign foundCampaign = prospectingCampaigns.isEmpty() ? null : prospectingCampaigns.get(0);

        return Optional.ofNullable(foundCampaign);
    }

    private Optional<Long> findCampaignWithLastClickAction(List<History> histories, List<Campaign> campaigns) {
        Map<Long, List<History>> historyGropedByCampaigns = histories.stream()
                .filter(history -> ACTION_TO_ATTRIBUTE.equals(history.getAction().toLowerCase()))
                .collect(Collectors.groupingBy(History::getCampaignId));

        List<History> historiesWithLastEvents = new ArrayList<>();
        for (final Campaign campaign : campaigns) {
            List<History> historiesOfCertainCampaign = historyGropedByCampaigns.get(campaign.getCampaignId());
            History history = historiesOfCertainCampaign.stream().filter(h -> campaign.isActive(h.getDate()))
                    .max(Comparator.comparing(History::getDate)).get();
            historiesWithLastEvents.add(history);
        }

        historiesWithLastEvents.sort(Comparator.comparing(History::getDate));
        Long foundCampaign = historiesWithLastEvents.isEmpty() ? null : historiesWithLastEvents.get(0).getCampaignId();

        return Optional.ofNullable(foundCampaign);
    }

}
