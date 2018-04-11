package com.tracktik.test;

import com.tracktik.scheduler.domain.Employee;
import com.tracktik.scheduler.domain.EmployeeConstraintMultiplier;
import com.tracktik.scheduler.domain.Shift;
import com.tracktik.scheduler.domain.TimeSlot;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameMatchesAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class TagValueTest extends ConstraintRuleTestBase {

  @Test
  public void hasNoTags() {

    Employee employee = new Employee().setId("1");

    Shift shift1 = new Shift()
        .setId("1")
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("SHIFT_TAG"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasOneTag() {

    Set<String> tags = new HashSet<>();
    tags.add("tag1");

    Map<String, Long> tagValues = new HashMap<>();
    tagValues.put("tag1", 5L);

    Employee employee = new Employee().setId("1").setTagValues(tagValues);

    Shift shift1 = new Shift()
        .setId("1")
        .setTags(tags)
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("SHIFT_TAG"));

    assertEquals(5L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasNoTagMatch() {

    Set<String> tags = new HashSet<>();
    tags.add("tag1");

    Map<String, Long> tagValues = new HashMap<>();
    tagValues.put("tag2", 5L);

    Employee employee = new Employee().setId("1").setTagValues(tagValues);

    Shift shift1 = new Shift()
        .setId("1")
        .setTags(tags)
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("SHIFT_TAG"));

    assertEquals(0L, getScoreHolder().getSoftScore());
  }

  @Test
  public void hasMultipleTagMatch() {

    Set<String> tags = new HashSet<>();
    tags.add("tag1");
    tags.add("tag2");

    Map<String, Long> tagValues = new HashMap<>();
    tagValues.put("tag1", 5L);
    tagValues.put("tag2", 15L);

    Employee employee = new Employee().setId("1").setTagValues(tagValues);

    Shift shift1 = new Shift()
        .setId("1")
        .setTags(tags)
        .setEmployee(employee)
        .setStart(LocalDateTime.parse("2018-01-17 01:00:00", dtf))
        .setEnd(LocalDateTime.parse("2018-01-17 02:00:00", dtf));

    ksession.insert(employee);
    ksession.insert(shift1);

    ksession.fireAllRules(new RuleNameEqualsAgendaFilter("SHIFT_TAG"));

    assertEquals(20L, getScoreHolder().getSoftScore());
  }


}

