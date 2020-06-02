package jp.co.tis.phr.web.controller.healthdata;

import jp.co.tis.phr.core.dao.UserRecordDao;
import jp.co.tis.phr.core.entity.*;
import jp.co.tis.phr.web.constant.Term;
import jp.co.tis.phr.web.controller.login.LoginUserDetails;
import jp.co.tis.phr.web.model.BloodPressure;
import jp.co.tis.phr.web.model.BodyTemperature;
import jp.co.tis.phr.web.model.PulseRate;
import jp.co.tis.phr.web.model.SpO2;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@SessionAttributes(names = {"userRecord"})
@AllArgsConstructor
public class HealthDataGraphRestController {

    private final static String TERM_FROM_KEY = "from";
    private final static String TERM_TO_KEY = "to";

    private final UserRecordDao userRecordDao;

    @GetMapping("/body-temperature/graph")
    public List<BodyTemperature> graphBodyTemperature(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                                      @RequestParam("term") String term,
                                                      @RequestParam("timezone") String timezone) {

        return listEventsWithinTerm(loginUserDetails.getUser().getUserId(), term, timezone)
                .stream()
                .map(BodyTemperature::extractBodyTemperature)
                .filter(bt -> bt.getBodyTemperature() != null)
                .sorted(Comparator.comparing(BodyTemperature::getTime))
                .collect(Collectors.toList());
    }

    @GetMapping("/blood-pressure/graph")
    public List<BloodPressure> graphBloodPressure(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                                  @RequestParam("term") String term,
                                                  @RequestParam("timezone") String timezone) {
        return listEventsWithinTerm(loginUserDetails.getUser().getUserId(), term, timezone)
                .stream()
                .map(BloodPressure::extractBloodPressure)
                .filter(bp -> bp.getDiastolicBloodPressure() != null && bp.getSystolicBloodPressure() != null)
                .sorted(Comparator.comparing(BloodPressure::getTime))
                .collect(Collectors.toList());
    }

    @GetMapping("/pulse-rate/graph")
    public List<PulseRate> graphPulseRate(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                          @RequestParam("term") String term,
                                          @RequestParam("timezone") String timezone) {
        return listEventsWithinTerm(loginUserDetails.getUser().getUserId(), term, timezone)
                .stream()
                .map(PulseRate::extractPulseRate)
                .filter(pr -> pr.getPulseRate() != null)
                .sorted(Comparator.comparing(PulseRate::getTime))
                .collect(Collectors.toList());
    }

    @GetMapping("/sp-o2/graph")
    public List<SpO2> graphSpO2(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                @RequestParam("term") String term,
                                @RequestParam("timezone") String timezone) {
        return listEventsWithinTerm(loginUserDetails.getUser().getUserId(), term, timezone)
                .stream()
                .map(SpO2::extractSpO2)
                .filter(sp -> sp.getSpO2() != null)
                .sorted(Comparator.comparing(SpO2::getTime))
                .collect(Collectors.toList());
    }

    private Map<String, ZonedDateTime> getDateTimeFromTerm(String term, String timezone) {
        HashMap<String, ZonedDateTime> terms = new HashMap<>();

        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime todayWithSpecifiedTimezone = today.withZoneSameInstant(ZoneId.of(timezone));

        if (Term.TODAY.getTerm().equals(term)) {
            terms.put(TERM_FROM_KEY, todayWithSpecifiedTimezone);
            terms.put(TERM_TO_KEY, todayWithSpecifiedTimezone.plusDays(1).minusSeconds(1));
        } else if (Term.WEEK.getTerm().equals(term)) {
            terms.put(TERM_FROM_KEY, todayWithSpecifiedTimezone.minusWeeks(1));
            terms.put(TERM_TO_KEY, todayWithSpecifiedTimezone.plusDays(1).minusSeconds(1));
        } else {
            terms.put(TERM_FROM_KEY, todayWithSpecifiedTimezone.minusWeeks(2));
            terms.put(TERM_TO_KEY, todayWithSpecifiedTimezone.plusDays(1).minusSeconds(1));
        }
        return terms;
    }

    private List<UserRecord> listEventsWithinTerm(String userId, String term, String timezone) {
        Map<String, ZonedDateTime> terms = getDateTimeFromTerm(term, timezone);
        return userRecordDao.findByEventTime(userId, terms.get(TERM_FROM_KEY), terms.get(TERM_TO_KEY));
    }

}
