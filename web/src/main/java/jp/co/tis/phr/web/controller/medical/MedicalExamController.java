package jp.co.tis.phr.web.controller.medical;

import jp.co.tis.phr.core.dao.UserRecordDao;
import jp.co.tis.phr.core.division.RecordType;
import jp.co.tis.phr.core.division.Reliability;
import jp.co.tis.phr.core.entity.Event;
import jp.co.tis.phr.core.entity.UserRecord;
import jp.co.tis.phr.web.controller.login.LoginUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Controller
@SessionAttributes(names = {"userRecord"})
@AllArgsConstructor
public class MedicalExamController {

    private final MessageSource messageSource;

    private final UserRecordDao userRecordDao;

    private final static Map<String, String> TOBACCO_HISTORY = Collections.unmodifiableMap(new LinkedHashMap<>() {
        {
            put("あり", "あり");
            put("なし", "なし");
            put("過去にあり", "過去にあり");
        }
    });

    private final static Map<String, String> URINE_PROTEIN = Collections.unmodifiableMap(new LinkedHashMap<>() {
        {
            put("－", "－");
            put("±", "±");
            put("＋", "＋");
            put("2＋", "2＋");
            put("3＋以上", "3＋以上");
        }
    });

    private final static Map<String, String> URINARY_GLUCOSE = Collections.unmodifiableMap(new LinkedHashMap<>() {
        {
            put("－", "－");
            put("±", "±");
            put("＋", "＋");
            put("2＋以上", "2＋以上");
        }
    });

    @GetMapping(path = "/medical-exam/list")
    public String list(@AuthenticationPrincipal LoginUserDetails loginUserDetails, Model model) {
        List<UserRecord> userRecords = userRecordDao.findMedicalExam(loginUserDetails.getUser().getUserId());
        model.addAttribute("userRecords", userRecords);
        return "medical-exam/medical-exam-list";
    }


    @GetMapping(path = "/medical-exam/create")
    public String create(MedicalExamForm medicalExamForm, Model model) {
        model.addAttribute("TOBACCO_HISTORY", TOBACCO_HISTORY);
        model.addAttribute("URINE_PROTEIN", URINE_PROTEIN);
        model.addAttribute("URINARY_GLUCOSE", URINARY_GLUCOSE);
        return "medical-exam/medical-exam-create";
    }

    @GetMapping(path = "/medical-exam/{id}/edit")
    public String edit(@PathVariable String id,
                       MedicalExamForm medicalExamForm,
                       Model model) {
        UserRecord userRecord = userRecordDao.findById(id);
        BeanUtils.copyProperties(userRecord, medicalExamForm);
        medicalExamForm.setEventTime(userRecord.getEvent().getTime().toLocalDate());
        medicalExamForm.setEventLocation(userRecord.getEvent().getLocation());

        model.addAttribute("userRecord", userRecord);
        model.addAttribute("TOBACCO_HISTORY", TOBACCO_HISTORY);
        model.addAttribute("URINE_PROTEIN", URINE_PROTEIN);
        model.addAttribute("URINARY_GLUCOSE", URINARY_GLUCOSE);

        return "medical-exam/medical-exam-edit";
    }

    @PostMapping(path = "/medical-exam/_/create")
    public String save(@Validated MedicalExamForm medicalExamForm,
                       BindingResult result,
                       @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                       Model model,
                       RedirectAttributes attributes, Locale locale) {
        if (result.hasErrors()) {
            return create(medicalExamForm, model);
        }

        log.info(medicalExamForm.toString());

        UserRecord userRecord = new UserRecord();
        BeanUtils.copyProperties(medicalExamForm, userRecord);
        userRecord.setUserId(loginUserDetails.getUser().getUserId());
        Event event = new Event();
        event.setRecordType(RecordType.MEDICAL_EXAM.getRecordType());
        event.setReliability(Reliability.LEVEL_3.getLevel());
        // cosmosに入れるときにはUTCで登録する
        event.setTime(medicalExamForm.getEventTime().atTime(LocalTime.MIN).atZone(ZoneId.of("UTC")));
        event.setPublish(ZonedDateTime.now(ZoneId.of("UTC")));

        event.setLocation(medicalExamForm.getEventLocation());
        userRecord.setEvent(event);
        userRecordDao.save(userRecord);

        attributes.addFlashAttribute("message",
                messageSource.getMessage("medical-exam.create.message", null, locale));

        return "redirect:/medical-exam/complete";
    }

    @PostMapping(path = "/medical-exam/{id}/save")
    public String save(@PathVariable String id,
                       @Validated MedicalExamForm medicalExamForm,
                       BindingResult result,
                       @ModelAttribute("userRecord") UserRecord beforeUserRecord,
                       Model model,
                       RedirectAttributes attributes, Locale locale) {

        if (result.hasErrors()) {
            return edit(id, medicalExamForm, model);
        }

        log.info(medicalExamForm.toString());

        UserRecord userRecord = new UserRecord();
        BeanUtils.copyProperties(medicalExamForm, userRecord);
        userRecord.setUserId(beforeUserRecord.getUserId());
        userRecord.setEvent(beforeUserRecord.getEvent());
        userRecord.getEvent().setPublish(ZonedDateTime.now(ZoneId.of("UTC")));
        userRecordDao.save(userRecord);

        attributes.addFlashAttribute("message",
                messageSource.getMessage("medical-exam.edit.message", null, locale));

        return "redirect:/medical-exam/complete";
    }


    @PostMapping(path = "/medical-exam/{id}/delete")
    public String save(@PathVariable String id,
                       RedirectAttributes attributes, Locale locale) {

        UserRecord ur = userRecordDao.findById(id);
        ur.getEvent().setPublish(ZonedDateTime.now(ZoneId.of("UTC")));
        userRecordDao.deleteAfterTTL(ur, 60);

        attributes.addFlashAttribute("message",
                messageSource.getMessage("medical-exam.delete.message", null, locale));

        return "redirect:/medical-exam/complete";
    }

    @GetMapping(path = "/medical-exam/complete")
    public String complete(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "/medical-exam/medical-exam-complete";
    }
}
