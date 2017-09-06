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
        attributeToCampaign(purchaseDate, histories, campaigns);
        return findCampaignWithLastClickAction(purchaseDate, histories, campaigns);
    }

    private long findCampaignWithLastClickAction(Date purchaseDate, List<History> histories, List<Campaign> campaigns) {
        Optional<History> history = histories.stream().filter(h -> purchaseDate.after(h.getDate())).sorted(Comparator.comparing(History::getDate)).findFirst();
        return history.isPresent() ? history.get().getCampaignId() : NOT_FOUND;
    }

    private void attributeToCampaign(Date purchaseDate, List<History> histories, List<Campaign> campaigns) {
        Optional<Campaign> prospectingCampaign = findActiveProspectingCampaign(purchaseDate, campaigns);
        if (prospectingCampaign.isPresent()) {
            prospectingCampaign.get().attribute();
        } else {
            Optional<Campaign> activeCampaign = findActiveCampaign(histories, campaigns);
            if (activeCampaign.isPresent()) {
                activeCampaign.get().attribute();
            }
        }
    }

    private Optional<Campaign> findActiveProspectingCampaign(Date purchaseDate, List<Campaign> campaigns) {
        List<Campaign> prospectingCampaigns = campaigns.stream().
                filter(campaign -> campaign.getType() == CampaignType.PROSPECTING
                        && campaign.isActive(purchaseDate)).collect(Collectors.toList());
        Campaign foundCampaign = prospectingCampaigns.isEmpty() ? null : prospectingCampaigns.get(0);

        return Optional.ofNullable(foundCampaign);
    }

    private Optional<Campaign> findActiveCampaign(List<History> histories, List<Campaign> campaigns) {// todo: need refactoring
        Map<Long, List<History>> historyGropedByCampaigns = histories.stream()
                .filter(history -> ACTION_TO_ATTRIBUTE.equals(history.getAction().toLowerCase()))
                .collect(Collectors.groupingBy(History::getCampaignId));

        History historiesWithLastEvents = null;
        for (final Campaign campaign : campaigns) {
            List<History> historiesOfCertainCampaign = historyGropedByCampaigns.get(campaign.getCampaignId());
            History history = historiesOfCertainCampaign.stream().filter(h -> campaign.isActive(h.getDate()))
                    .max(Comparator.comparing(History::getDate)).get();

            if(historiesWithLastEvents == null) {
                historiesWithLastEvents = history;
            } else {
                historiesWithLastEvents = history.getDate().after(historiesWithLastEvents.getDate()) ? history : historiesWithLastEvents;
            }

        }

        Campaign foundCampaign = null;
        if (historiesWithLastEvents != null) {
            foundCampaign = findCampaign(campaigns, historiesWithLastEvents.getCampaignId());
        }

        return Optional.ofNullable(foundCampaign);
    }

    private Campaign findCampaign(List<Campaign> campaigns, long id) {
        return campaigns.stream().filter(campaign -> campaign.getCampaignId() == id).findAny().get();
    }
}
