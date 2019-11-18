package com.tracktik.scheduler.util;

import com.tracktik.scheduler.api.domain.RequestForScheduling;
import com.tracktik.scheduler.api.domain.RequestOvertimeRule;
import com.tracktik.scheduler.configuration.*;
import com.tracktik.scheduler.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

public class RequestResponseMapper {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final Logger logger = LoggerFactory.getLogger(RequestResponseMapper.class);

  private static Long distance(Double geo1_latitude, Double geo1_longitude, Double geo2_latitude, Double geo2_longitude) {
    Double distance = (Math.acos(Math.sin(geo1_latitude * Math.PI / 180D) * Math.sin(geo2_latitude * Math.PI / 180D) + Math.cos(geo1_latitude * Math.PI / 180D) * Math.cos(geo2_latitude * Math.PI / 180D) * Math.cos((geo1_longitude - geo2_longitude) * Math.PI / 180D)) * 180 / Math.PI) * 60 * 1.1515D;
    return distance.longValue();
  }

  public static Schedule requestToSchedule(String id, RequestForScheduling request) {

    Schedule schedule = new Schedule();
    schedule.setId(id);

    Set<Skill> skillSet = request.skills.stream().parallel().map(requestSkill -> new Skill(requestSkill.id, requestSkill.description)).collect(Collectors.toSet());
    Set<Scale> scaleSet = request.scales.stream().parallel().map(requestScale -> new Scale(requestScale.id, ScaleTag.valueOf(requestScale.tag), Integer.parseInt(requestScale.rating))).collect(Collectors.toSet());

    schedule.setSites(
        request.sites.stream().map(old -> new Site()
            .setId(old.id)
            .setName(old.name)
            .setLatitude(StringUtils.isBlank(old.geo_lat) ? null : new Double(old.geo_lat))
            .setLongitude(StringUtils.isBlank(old.geo_lon) ? null : new Double(old.geo_lon))).collect(Collectors.toSet())
    );

    //Map<String, Skill> skillsMap = skills.stream().collect(Collectors.toMap(skill -> skill.id, skill -> new Skill(skill.id, skill.description)));

    schedule.setPosts(request.posts.stream().map(old -> {
        //  logger.debug("mapping post: {}", old);
          Post post = new Post();
          if (!StringUtils.isBlank(old.bill_rate)) {
            Float billRate = new Float(old.bill_rate);
            billRate = billRate * 100;
            post.setBillRate(billRate.longValue());
          }
          if (!StringUtils.isBlank(old.pay_rate)) {
            Float payRate = new Float(old.pay_rate);
            payRate = payRate * 100;
            post.setPayRate(payRate.longValue());
          }
          post.setId(old.id)
              .setName(old.name);
          if (old.pay_type.equals(PayType.EMPLOYEE_RATE.name())) post.setPayType(PayType.EMPLOYEE_RATE);
          if (old.pay_type.equals(PayType.POST_RATE.name())) post.setPayType(PayType.POST_RATE);
          post.setSite(
              schedule.getSites().stream().parallel().filter(site -> site.getId().equals(old.site_id)).findAny().get()
          );

          Set<String> hardSkillIds = request.post_skills.stream().parallel().filter(skill -> skill.post_id.equals(old.id) && skill.type.equals(SkillType.HARD.name())).map(skill -> skill.skill_id).collect(Collectors.toSet());
          Set<String> softSkillIds = request.post_skills.stream().parallel().filter(skill -> skill.post_id.equals(old.id) && skill.type.equals(SkillType.SOFT.name())).map(skill -> skill.skill_id).collect(Collectors.toSet());

          post.setHardSkills(
              skillSet.stream().parallel().filter(skill -> hardSkillIds.contains(skill.getId())).collect(Collectors.toSet())
          );
          post.setSoftSkills(
              skillSet.stream().parallel().filter(skill -> softSkillIds.contains(skill.getId())).collect(Collectors.toSet())
          );
          return post;
        }).collect(Collectors.toSet())
    );

    request.employees.forEach(employee -> {
      //logger.debug("Mapping employee: {}", employee);
      schedule.addEmployee(
          new Employee()
              .setId(employee.id)
              .setAvailabilityPreference(null)  //TODO account
              .setCostFromFloatString(employee.pay_rate)
              .setName(employee.name)
              .setOvertimeRuleId(employee.overtime_rule_id)
              .setPayScheduleId(employee.pay_schedule_id)
              .setPreviousPayPeriodEnd(StringUtils.isBlank(employee.previous_period_last_end_date_time) ? null : LocalDateTime.parse(employee.previous_period_last_end_date_time, dateTimeFormatter))
              .setPreferredHours(StringUtils.isBlank(employee.preferred_hours) ? null : new Long(employee.preferred_hours))
              .setLatitude(StringUtils.isBlank(employee.geo_lat) ? null : new Double(employee.geo_lat))
              .setLongitude(StringUtils.isBlank(employee.geo_lon) ? null : new Double(employee.geo_lon))
              .setSiteExperience(
                  request.employees_to_sites.stream().parallel()
                      .filter(employee_to_site -> employee_to_site.user_id.equals(employee.id))
                      .map(employee_to_site -> employee_to_site.site_id)
                      .map(site_id ->
                          schedule.getSites().stream().filter(site -> site.getId().equals(site_id)).findAny().get()
                      ).collect(Collectors.toList())
              )
              .setPostExperience(
                  request.employees_to_posts.stream().parallel()
                      .filter(employee_to_post -> employee_to_post.user_id.equals(employee.id))
                      .map(employee_to_post -> employee_to_post.post_id)
                      .map(post_id ->
                          schedule.getPosts().stream().filter(site -> site.getId().equals(post_id)).findAny().get()
                      ).collect(Collectors.toList())
              )
              .setSkills(
                  request.employee_skills.stream().parallel()
                      .filter(employee_skill -> employee_skill.employee_id.equals(employee.id))
                      .map(employee_skill -> employee_skill.skill_id)
                      .map(skill_id -> skillSet.stream().filter(skill -> skill.getId().equals(skill_id)).findAny().get())
                      .collect(Collectors.toList())
              )
              .setScales(
                      request.employee_scales.stream().parallel()
                              .filter(employee_scale -> employee_scale.employee_id.equals(employee.id))
                              .map(employee_scale -> employee_scale.scale_id)
                              .map(scale_id -> scaleSet.stream().filter(scale -> scale.getId().equals(scale_id)).findAny().get())
                              .collect(Collectors.toList())
              )
              .setSeniority(StringUtils.isBlank(employee.seniority) ? null : new Integer(employee.seniority))
              .setMinimumRestPeriod(employee.minimum_rest_period == null ? new Long(8) : new Long(employee.minimum_rest_period))
      );
    });

    schedule.setShifts(
        request.shifts.stream().map(old -> {
          //logger.debug("Request shift being parsed: {}", old);
          //Make sure the end is exclusive
          Shift shift = new Shift()
              .setId(old.shift_id)
              .setPlan(old.plan == null || old.plan.equals("1"))
              .setStart(LocalDateTime.parse(old.start_date_time, dateTimeFormatter))
              .setEnd(LocalDateTime.parse(old.end_date_time, dateTimeFormatter).minus(1L, SECONDS))
              .setStartTimeStamp(old.start_timestamp)
              .setEndTimeStamp(old.end_timestamp)
              .setDuration(new Float(old.duration))
              .setPost(
                  schedule.getPosts().stream().filter(post -> post.getId().equals(old.post_id)).findAny().get()
              );
          if (old.employee_id != null && !shift.getPlan()) {
            schedule.getEmployees().stream().filter(employee -> employee.getId().equals(old.employee_id)).findAny().ifPresent(shift::setEmployee);
          }
          return shift;
        }).collect(Collectors.toSet())
    );

    schedule.setTimesOff(request.time_off.stream().map(requestTimeOff -> {
      return new TimeOff()
          .setEmployeeId(requestTimeOff.employee_id)
          .setStart(LocalDateTime.ofInstant(Instant.ofEpochMilli(new Long(requestTimeOff.start_time)), ZoneId.systemDefault()))
          .setEnd(LocalDateTime.ofInstant(Instant.ofEpochMilli(new Long(requestTimeOff.end_time)), ZoneId.systemDefault()));
    }).collect(Collectors.toSet()));

    schedule.setEmployeeAvailabilities(
        request.employee_availabilities.stream().map(requestEmployeeAvailability -> {
          return new EmployeeAvailability()
              .setEmployeeId(requestEmployeeAvailability.employee_id)
              .setType(AvailabilityType.valueOf(requestEmployeeAvailability.type))
              .setDayOfWeek(DayOfWeek.of(new Integer(requestEmployeeAvailability.day_of_week)))
              .setStartSeconds(new Long(requestEmployeeAvailability.seconds_start))
              .setEndSeconds(new Long(requestEmployeeAvailability.seconds_end))
              .setStartTime(LocalTime.MIDNIGHT.plus(new Long(requestEmployeeAvailability.seconds_start), ChronoUnit.SECONDS))
              //The end time will be exclusive
              .setEndTime(LocalTime.MIDNIGHT.plus(new Long(requestEmployeeAvailability.seconds_end) - 1, ChronoUnit.SECONDS));
        }).collect(Collectors.toSet())
    );

    schedule.getEmployees().stream().filter(employee -> employee.getLatitude() != null && employee.getLongitude() != null).forEach(employee -> {
      schedule.getSites().stream().filter(site -> site.getLatitude() != null && site.getLongitude() != null).forEach(site -> {
        Long distance = distance(employee.getLatitude(), employee.getLongitude(), site.getLatitude(), site.getLongitude());
        schedule.getEmployeeSiteDistance().add(new EmployeeSiteDistance(employee.getId(), site.getId(), distance));
      });
    });

    //logger.info("distances: " + schedule.getEmployeeSiteDistance());

    schedule.setKeyValueFacts(
        request.facts.entrySet().stream().map(entry -> {
          return new KeyValueFact().setKey(entry.getKey()).setValue(entry.getValue());
        }).collect(Collectors.toSet())
    );

    schedule.setSiteBans(
        request.site_bans.stream().map(ban -> new SiteBan().setEmployeeId(ban.employee_id).setSiteId(ban.site_id)).collect(Collectors.toSet())
    );

    schedule.setEmployeeConstraintMultipliers(
        request.employee_multipliers.entrySet().stream().flatMap(stringMapEntry -> {
          String employeeId = stringMapEntry.getKey();
          return stringMapEntry.getValue().entrySet().stream().map(stringStringEntry -> {
            return new EmployeeConstraintMultiplier()
                .setEmployeeId(employeeId)
                .setName(stringStringEntry.getKey())
                .setMultiplier(new Double(stringStringEntry.getValue()));
          });
        }).collect(Collectors.toSet())
    );

    Set<RequestOvertimeRule> perPeriodOvertimeRules = request.overtime_rules.stream().filter(requestOvertimeRule -> requestOvertimeRule.rule.containsKey("RULE_HOURS_PER_PERIOD")).collect(Collectors.toSet());
    perPeriodOvertimeRules.forEach(requestOvertimeRule -> {
      requestOvertimeRule.rule.values().forEach(ruleList -> {
          ruleList.forEach(list -> {
            schedule.getPeriodOvertimeDefinitions().add(new PeriodOvertimeDefinition()
                .setName(requestOvertimeRule.name)
                .setId(requestOvertimeRule.id)
                .setMinimumHours((long) (((Integer) list.get(0))))
                .setMaximumHours(list.get(1).equals("INF") ? Long.MAX_VALUE : (long) (((Integer) list.get(1))))
                .setOvertimeType((String)list.get(2)));
          });
      });
    });

    Set<RequestOvertimeRule> perDayOvertimeRules = request.overtime_rules.stream().filter(requestOvertimeRule -> requestOvertimeRule.rule.containsKey("RULE_HOURS_PER_DAY")).collect(Collectors.toSet());
    perDayOvertimeRules.forEach(requestOvertimeRule -> {
      requestOvertimeRule.rule.values().forEach(ruleList -> {
        ruleList.forEach(list -> {
          schedule.getDayOvertimeDefinitions().add(new DayOvertimeDefinition()
              .setName(requestOvertimeRule.name)
              .setId(requestOvertimeRule.id)
              .setMinimumHours(new Long(((Integer) list.get(0))))
              .setMaximumHours(list.get(1).equals("INF") ? Long.MAX_VALUE : new Long(((Integer) list.get(1))))
              .setOvertimeType((String)list.get(2)));
        });
      });
    });

    Set<RequestOvertimeRule> consecutiveDaysOvertimeRules = request.overtime_rules.stream().filter(requestOvertimeRule -> requestOvertimeRule.rule.containsKey("RULE_CONSECUTIVE_DAYS")).collect(Collectors.toSet());
    consecutiveDaysOvertimeRules.forEach(requestOvertimeRule -> {
      requestOvertimeRule.rule.values().forEach(ruleList -> {
        ruleList.forEach(list -> {
          schedule.getConsecutiveDaysOvertimeDefinitions().add(new ConsecutiveDaysOvertimeDefinition()
              .setName(requestOvertimeRule.name)
              .setId(requestOvertimeRule.id)
              .setMinimumDay(new Long(((Integer) list.get(0))))
              .setMaximumDay(list.get(1).equals("INF") ? Long.MAX_VALUE : new Long(((Integer) list.get(1))))
              .setOvertimeType((String)list.get(2))
              .setMinimumHours(new Long(((Integer) list.get(0))))
              .setMaximumHours(list.get(1).equals("INF") ? Long.MAX_VALUE : new Long(((Integer) list.get(1)))));
        });
      });
    });

    schedule.setPayrollSchedules(request.payroll_schedules.stream().map(requestPayrollSchedule -> {

      return new PayrollSchedule()
          .setId(requestPayrollSchedule.id)
          .setName(requestPayrollSchedule.name)
          .setAlignHolidaysWithPeriodStartTime(requestPayrollSchedule.align_holidays_with_period_start_time.equals("1"))
          .setCountHolidayHoursTowardsPeriodOvertime(requestPayrollSchedule.count_holiday_hours_towards_period_ot.equals("1"))
          .setOverlappingMethod(requestPayrollSchedule.overlapping_method.equals("cutHoursRule") ? OverlappingMethodType.CUT : OverlappingMethodType.SPAN)
          .setFrequency(requestPayrollSchedule.frequency)
          .setPeriodStartDate(LocalDate.parse(requestPayrollSchedule.period_start_date))
          .setPeriodStartTime(LocalTime.parse(requestPayrollSchedule.period_start_time));
    }).collect(Collectors.toSet()));

    schedule.setHolidayPeriods(request.holidays.stream().map(requestHoliday -> {
      return new HolidayPeriod()
          .setPostId(requestHoliday.post_id)
          .setStart(LocalDateTime.ofEpochSecond(new Long(requestHoliday.start_timestamp), 0, OffsetDateTime.now().getOffset()))
          .setEnd(LocalDateTime.ofEpochSecond(new Long(requestHoliday.start_timestamp), 0, OffsetDateTime.now().getOffset()));
    }).collect(Collectors.toSet()));

    schedule.setConfigFacts(request.requestFacts.stream().map(requestFact -> {
        logger.debug("Request facts being parsed: {}", requestFact);
        FactType factType = requestFact.type.asFactType();
        ConfigFact configFact = new ConfigFact();
        switch (factType){
            case CLOSE_BY_SITE:
                configFact = new CloseBySite(requestFact.active, requestFact.impact, requestFact.isHardImpact, requestFact.definition);
                break;
            case FAR_FROM_SITE:
                configFact = new FarFromSite(requestFact.active, requestFact.impact, requestFact.isHardImpact, requestFact.definition);
                break;
            case FAIRLY_FAR_FROM_SITE:
                configFact = new FairlyFarFromSite(requestFact.active, requestFact.impact, requestFact.isHardImpact, requestFact.definition);
                break;
            case HARD_SKILL_MISSING:
                configFact = new HardSkillMissing(requestFact.impact, requestFact.active, requestFact.isHardImpact, requestFact.isHardFailure);
                break;
            case LESS_THAN_EXPECTED_HOURS:
                configFact = new LessThanExpectedHours(requestFact.impact, requestFact.active, requestFact.isHardImpact);
                break;
            case MAYBE_AVAILABLE:
                configFact = new MaybeAvailable(requestFact.impact, requestFact.active);
                break;
            case MINIMUM_REST_PERIOD:
                configFact = new MinimumRestPeriod(requestFact.impact, requestFact.active, requestFact.isHardImpact);
                break;
            case MORE_THAN_EXPECTED_HOURS:
                configFact = new MoreThanExpectedHours(requestFact.impact, requestFact.active, requestFact.isHardImpact);
                break;
            case NO_EXPERIENCE_AT_SITE:
                configFact = new NoExperienceAtSite(requestFact.impact, requestFact.active, requestFact.isHardImpact);
                break;
            case NOT_ASSIGNED_TO_SITE:
                configFact = new NotAssignedToSite(requestFact.impact, requestFact.active, requestFact.isHardImpact);
                break;
            case NOT_AVAILABLE:
                configFact = new NotAvailable(requestFact.impact, requestFact.isHardImpact, requestFact.active);
                break;
            case SOFT_SKILL_MISSING:
                configFact = new SoftSkillMissing(requestFact.impact, requestFact.active, requestFact.isHardImpact);
                break;
        }
        return configFact;
    }).collect(Collectors.toSet()));

      schedule.setScaleFacts(request.scaleFacts.stream().map(requestScaleFact -> {
          logger.debug("Scale facts being parsed: {}", requestScaleFact);
          return new ScaleFact()
                  .setScaleTag(ScaleTag.valueOf(requestScaleFact.scaleTag))
                  .setScaleType(ScaleType.valueOf(requestScaleFact.scaleType))
                  .setRating(Integer.parseInt(requestScaleFact.rating))
                  .setPostId(Integer.parseInt(requestScaleFact.post_id))
                  .setImpact(new Impact(Boolean.parseBoolean(requestScaleFact.scaleImpactSquare), Integer.parseInt(requestScaleFact.scaleImpact)));
      }).collect(Collectors.toSet()));


    return schedule;
  }

}
