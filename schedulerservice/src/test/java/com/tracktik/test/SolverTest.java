package com.tracktik.test;

import com.tracktik.scheduler.api.domain.*;
import com.tracktik.scheduler.domain.Schedule;
import com.tracktik.scheduler.domain.SchedulingResponse;
import com.tracktik.scheduler.domain.SolverStatus;
import com.tracktik.scheduler.util.RequestResponseMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.drools.core.rule.Collect;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
public class SolverTest {

  protected static SolverFactory<Schedule> solverFactory;
  protected Solver<Schedule> solver;

  class Location {
    Location(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
    }
    public double latitude;
    public double longitude;
  }
  class BaseShift {
    LocalTime startTime;
    LocalTime endTime;

    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

    BaseShift() {}
    public BaseShift shiftDate(LocalDate date) {
      startDateTime = LocalDateTime.of(date, startTime);
      if (endTime.isAfter(startTime)) {
        endDateTime = LocalDateTime.of(date, endTime);
      } {
        //Actually ends on the next day
        endDateTime = LocalDateTime.of(date.plus(1L, ChronoUnit.DAYS), endTime);
      }
      return this;
    }

    long duration() {
      return Duration.between(startDateTime, endDateTime).toHours();
    }

  }
  class FirstShift extends BaseShift {
    FirstShift() {
      startTime = LocalTime.of(7, 0);
      endTime = LocalTime.of(15, 0);
    }
  }
  class SecondShift extends BaseShift {
    SecondShift() {
      startTime = LocalTime.of(15, 0);
      endTime = LocalTime.of(23, 0);
    }
  }
  class ThirdShift extends BaseShift {
    ThirdShift() {
      startTime = LocalTime.of(23, 0);
      endTime = LocalTime.of(7, 0);
    }
  }

  @BeforeClass
  public static void testingSetup() {
    solverFactory = SolverFactory.createFromXmlResource("schedulerConfig.xml");
  }

  @Before
  public void setupSolver() {
    SolverConfig config = solverFactory.getSolverConfig();
    TerminationConfig terminationConfig = config.getTerminationConfig();
    terminationConfig.setSecondsSpentLimit(5L);

    config.setTerminationConfig(terminationConfig);
    solver = solverFactory.buildSolver();
  }

  @After
  public void tearDown() {

  }

  @Test
  public void firstScheduleTest() {
    RequestForScheduling request = createRequestForScheduling();
    Schedule schedule = RequestResponseMapper.requestToSchedule(UUID.randomUUID().toString(), request);
    Schedule solvedSchedule = solver.solve(schedule);

    assert(solver.getBestScore() != null);
  }

  protected RequestForScheduling createRequestForScheduling() {
    RequestForScheduling request = new RequestForScheduling();
    request.skills = generateRequestSkills(50);
    request.sites = generateSites(15);
    request.posts = generatePostsForSites(request.sites, 3);
    request.employees = generateEmployees(40);
    request.shifts = generateShiftsForPosts(request.posts, LocalDate.now());
    request.site_bans = generateSiteBans(request.sites, request.employees);
    request.employee_availabilities = generateEmployeeAvailabilities(request.employees, 5);
    request.employee_skills = generateEmployeeSkills(request.employees, request.skills);
    request.employees_to_sites = generateEmployeeSiteExperience(request.employees, request.sites);
    request.post_skills = generatePostSkills(request.posts, request.skills);
    return request;
  }

  protected Set<RequestPostSkill> generatePostSkills(Set<RequestPost> posts, Set<RequestSkill> skills) {

    List<RequestSkill> skillList =new ArrayList<RequestSkill>(skills);
    List<String> skillTypes = Arrays.asList("HARD", "SOFT");

    return posts.stream().map(post -> {
      RequestPostSkill skill = new RequestPostSkill();
      skill.post_id = post.id;
      skill.skill_id = skillList.get(getRandomNumberInRange(0, skillList.size() - 1)).id;
      skill.type = skillTypes.get(getRandomNumberInRange(0,1));
      return skill;
    }).collect(Collectors.toSet());
  }

  //Might want this to add multiple sites
  protected Set<RequestEmployeeSiteAssignment> generateEmployeeSiteExperience(Set<RequestEmployee> employees, Set<RequestSite> sites) {

    List<RequestSite> siteList = new ArrayList<RequestSite>(sites);

    return employees.stream().map(employee -> {
      RequestEmployeeSiteAssignment assignment = new RequestEmployeeSiteAssignment();
      assignment.site_id = siteList.get(getRandomNumberInRange(0, siteList.size() - 1)).id;
      assignment.user_id = employee.id;
      return assignment;
    }).collect(Collectors.toSet());
  }

  //Might want to make this add multiple skills
  protected Set<RequestEmployeeSkill> generateEmployeeSkills(Set<RequestEmployee> employees, Set<RequestSkill> skills) {

    List<RequestSkill> skillList =new ArrayList<RequestSkill>(skills);

    return employees.stream().map(employee -> {
      RequestEmployeeSkill skill = new RequestEmployeeSkill();
      skill.employee_id = employee.id;
      skill.skill_id = skillList.get(getRandomNumberInRange(0, skills.size() - 1)).id;
      return skill;
    }).collect(Collectors.toSet());
  }

  protected Set<RequestEmployeeAvailability> generateEmployeeAvailabilities(Set<RequestEmployee> employees, int numberToSkip) {

    List<RequestEmployee> employeeList = new ArrayList<>(employees);
    List<String> availabilityTypes = Arrays.asList("NO", "MAYBE");
    return IntStream.range(0, employeeList.size())
        .filter(n -> n % numberToSkip == 0)
        .mapToObj(employeeList::get)
        .map(employee -> {
          RequestEmployeeAvailability availability = new RequestEmployeeAvailability();
          availability.day_of_week = Integer.toString(getRandomNumberInRange(1,6));
          availability.employee_id = employee.id;
          //some time in first half of day
          availability.seconds_start = Integer.toString(getRandomNumberInRange(1, 43200));
          //some time in second half of day
          availability.seconds_end = Integer.toString(getRandomNumberInRange(43201, 86399));
          availability.type = availabilityTypes.get(getRandomNumberInRange(0,1));

          return availability;
        })
        .collect(Collectors.toSet());
  }

  protected Set<RequestSiteBan> generateSiteBans(Set<RequestSite> sites, Set<RequestEmployee> employees) {

    Set<RequestSiteBan> bans = new HashSet<>();
    List<RequestSite> siteList = new ArrayList<>(sites);
    List<RequestEmployee> employeeList = new ArrayList<>(employees);

    RequestSite firstSite = siteList.get(0);
    RequestSite lastSite = siteList.get(siteList.size() - 1);

    RequestEmployee firstEmployee = employeeList.get(0);
    RequestEmployee lastEmployee = employeeList.get(siteList.size() - 1);

    RequestSiteBan ban = new RequestSiteBan();
    ban.employee_id = firstEmployee.id;
    ban.site_id = firstSite.id;
    bans.add(ban);

    ban = new RequestSiteBan();
    ban.employee_id = lastEmployee.id;
    ban.site_id = lastSite.id;
    bans.add(ban);

    return bans;
  }

  protected Set<RequestShift> generateShiftsForPosts(Set<RequestPost> posts, LocalDate startDate) {
    return posts.stream().map(post -> {
      return generateShifts(post.id, startDate);
    }).flatMap(Collection::stream).collect(Collectors.toSet());
  }
  protected Set<RequestShift> generateShifts(String postId, LocalDate startDate) {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd kk:mm:ss");
    Set<RequestShift> shifts = new HashSet<>();

    for (long dayOffset = 0; dayOffset < 7; dayOffset++) {
      LocalDate shiftDate = startDate.plus(dayOffset, ChronoUnit.DAYS);

      FirstShift firstShift = (FirstShift) new FirstShift().shiftDate(shiftDate);
      RequestShift firstRequestShift = new RequestShift();
      firstRequestShift.shift_id = RandomStringUtils.random(3, false, true);
      firstRequestShift.post_id = postId;
      firstRequestShift.plan = "1";
      firstRequestShift.start_date = shiftDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
      firstRequestShift.start_date_time = firstShift.startDateTime.format(dtf);
      firstRequestShift.end_date_time = firstShift.endDateTime.format(dtf);
      firstRequestShift.duration = Long.toString(firstShift.duration());
      firstRequestShift.start_timestamp = firstShift.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      firstRequestShift.end_timestamp = firstShift.endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      shifts.add(firstRequestShift);

      SecondShift secondShift = (SecondShift) new SecondShift().shiftDate(shiftDate);
      RequestShift secondRequestShift = new RequestShift();
      secondRequestShift.shift_id = RandomStringUtils.random(3, false, true);
      secondRequestShift.post_id = postId;
      secondRequestShift.plan = "1";
      secondRequestShift.start_date = shiftDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
      secondRequestShift.start_date_time = secondShift.startDateTime.format(dtf);
      secondRequestShift.end_date_time = secondShift.endDateTime.format(dtf);
      secondRequestShift.duration = Long.toString(secondShift.duration());
      secondRequestShift.start_timestamp = secondShift.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      secondRequestShift.end_timestamp = secondShift.endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      shifts.add(secondRequestShift);

      ThirdShift thirdShift = (ThirdShift) new ThirdShift().shiftDate(shiftDate);
      RequestShift thirdRequestShift = new RequestShift();
      thirdRequestShift.shift_id = RandomStringUtils.random(3, false, true);
      thirdRequestShift.post_id = postId;
      thirdRequestShift.plan = "1";
      thirdRequestShift.start_date = shiftDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
      thirdRequestShift.start_date_time = thirdShift.startDateTime.format(dtf);
      thirdRequestShift.end_date_time = thirdShift.endDateTime.format(dtf);
      thirdRequestShift.duration = Long.toString(thirdShift.duration());
      thirdRequestShift.start_timestamp = thirdShift.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      thirdRequestShift.end_timestamp = thirdShift.endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      shifts.add(thirdRequestShift);
    }

    return shifts;
  }

  protected Set<RequestEmployee> generateEmployees(int numberToGenerate) {
    return IntStream.rangeClosed(1, numberToGenerate)
        .mapToObj(number -> {
          RequestEmployee employee = new RequestEmployee();
          employee.id = Integer.toString(number);
          employee.name = "employee_" + number;
          Location location = randomLocationNearMontreal();
          employee.geo_lat = Double.toString(location.latitude);
          employee.geo_lon = Double.toString(location.longitude);
          employee.pay_rate = Integer.toString(getRandomNumberInRange(5, 20));
          employee.preferred_hours = Integer.toString(getRandomNumberInRange(25, 50));
          employee.seniority = Integer.toString(getRandomNumberInRange(5, 2000));
          return employee;
        }).collect(Collectors.toSet());
  }

  protected Set<RequestSkill> generateRequestSkills(int numberToGenerate) {
    return IntStream.rangeClosed(1, numberToGenerate)
        .mapToObj(number -> {
          RequestSkill skill = new RequestSkill();
          skill.id = Integer.toString(number);
          skill.description = "skill_" + number;
          return skill;
        }).collect(Collectors.toSet());
  }

  protected Set<RequestSite> generateSites(int numberToGenerate) {
    return IntStream.rangeClosed(1, numberToGenerate)
        .mapToObj(number -> {
          RequestSite site = new RequestSite();
          site.id = Integer.toString(number);
          site.name = "site_" + number;
          Location location = randomLocationNearMontreal();
          site.geo_lat = Double.toString(location.latitude);
          site.geo_lon = Double.toString(location.longitude);
          return site;
        }).collect(Collectors.toSet());
  }

  protected Set<RequestPost> generatePostsForSites(Set<RequestSite> sites, int maxNumberOfPosts) {

    return sites.stream().map(site -> {
      return generatePosts(maxNumberOfPosts, site.id);
    }).flatMap(Collection::stream).collect(Collectors.toSet());
  }

  protected Set<RequestPost> generatePosts(int maxNumberOfPosts, String siteId) {
    List<String> payTypes = new ArrayList<>();
    payTypes.add("EMPLOYEE_RATE");
    payTypes.add("POST_RATE");

    return IntStream.rangeClosed(1, getRandomNumberInRange(1, maxNumberOfPosts))
        .mapToObj(number -> {
          RequestPost post = new RequestPost();
          post.id = Integer.toString(number);
          post.site_id = siteId;
          post.name = "post_" + number;
          post.bill_rate = Integer.toString(getRandomNumberInRange(30, 60));
          post.pay_rate = Integer.toString(getRandomNumberInRange(5, 20));
          post.pay_type = payTypes.get(number % 2);
          return post;
        }).collect(Collectors.toSet());
  }

  public Location randomLocationNearMontreal() {
    double montreal_latitude = 45.517975;
    double montreal_longitude = -73.582340;
    return randomLocation(montreal_latitude, montreal_longitude, 60);
  }

  public Location randomLocation(double lat, double lon, int radiusInMeters) {
    Random random = new Random();

    // Convert radius from meters to degrees
    double radiusInDegrees = radiusInMeters / 111000f;

    double u = random.nextDouble();
    double v = random.nextDouble();
    double w = radiusInDegrees * Math.sqrt(u);
    double t = 2 * Math.PI * v;
    double x = w * Math.cos(t);
    double y = w * Math.sin(t);

    // Adjust the x-coordinate for the shrinking of the east-west distances
    double new_x = x / Math.cos(Math.toRadians(lon));

    double foundLongitude = new_x + lat;
    double foundLatitude = y + lon;

    return new Location(foundLatitude, foundLongitude);
  }

  private static int getRandomNumberInRange(int min, int max) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }
}
