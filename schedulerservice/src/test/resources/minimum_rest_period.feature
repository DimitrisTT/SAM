Feature: Minimum Rest Period

  Background:
    Given Minimum Rest Period being active is 'true'
    And its Minimum Rest Period score impact is '-50'
    And its Minimum Rest Period is hard impact is set to 'false'

  Scenario: testing rule firing for those with fewer than their minimum rest period in hours
    Given the following site with id '12' name 'bank' longitude '14.14' and latitude '15.15'
    And with payType 'EMPLOYEE_RATE'
    And the following soft skills
      | id | description |
      | 11 | charming    |
      | 42 | francophone |
    And the following hard skills
      | id | description     |
      | 33 | firearm license |
    And the following employees
      | id   | name      | preferred_hours  | geo_lat  | geo_long  | pay_rate  | seniority  | minimum_rest_period  | skillId |
      | 1111 | Andre     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1112 | Babara    | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1113 | Caspar    | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1114 | Danielle  | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1115 | Eduardo   | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1116 | Franciska | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1117 | Gregory   | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1118 | Helga     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1119 | Ivan      | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1120 | Julie     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
    And the following shift from '2018-01-18 09:00:00' to '2018-01-18 17:00:00' timestamp '1516284000' and end '1516312800' with duration '8.00' id '55' and plan 'true' available
    And a second shift from '2018-01-18 19:00:00' to '2018-01-18 23:00:00' timestamp '1516320000' and end '1516327200' with duration '4.00' id '56' and plan 'true' available
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | MINIMUM_REST_PERIOD | 1          |
      | 1112 | MINIMUM_REST_PERIOD | 1          |
      | 1113 | MINIMUM_REST_PERIOD | 1          |
      | 1114 | MINIMUM_REST_PERIOD | 1          |
      | 1115 | MINIMUM_REST_PERIOD | 1          |
      | 1116 | MINIMUM_REST_PERIOD | 1          |
      | 1117 | MINIMUM_REST_PERIOD | 1          |
      | 1118 | MINIMUM_REST_PERIOD | 1          |
      | 1119 | MINIMUM_REST_PERIOD | 1          |
      | 1120 | MINIMUM_REST_PERIOD | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 31400       |
      | 1112 | MAYBE                | 4           | 30400         | 31400       |
      | 1113 | MAYBE                | 4           | 30400         | 31400       |
      | 1114 | MAYBE                | 4           | 30400         | 31400       |
      | 1115 | MAYBE                | 4           | 30400         | 31400       |
      | 1116 | MAYBE                | 4           | 30400         | 31400       |
      | 1117 | MAYBE                | 4           | 30400         | 31400       |
      | 1118 | MAYBE                | 4           | 30400         | 31400       |
      | 1119 | MAYBE                | 4           | 30400         | 31400       |
      | 1120 | MAYBE                | 4           | 30400         | 31400       |
    And we apply each employee into the shift for the calculation
    And we apply each employee into the second shift for the calculation
    When Minimum Rest Period rules are calculated
    Then softscore is -2000


  Scenario: testing rule firing for those with more than their minimum rest period in hours
    Given the following site with id '12' name 'bank' longitude '14.14' and latitude '15.15'
    And with payType 'EMPLOYEE_RATE'
    And the following soft skills
      | id | description |
      | 11 | charming    |
      | 42 | francophone |
    And the following hard skills
      | id | description     |
      | 33 | firearm license |
    And the following employees
      | id   | name      | preferred_hours  | geo_lat  | geo_long  | pay_rate  | seniority  | minimum_rest_period  | skillId |
      | 1111 | Andre     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1112 | Babara    | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1113 | Caspar    | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1114 | Danielle  | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1115 | Eduardo   | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1116 | Franciska | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1117 | Gregory   | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1118 | Helga     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1119 | Ivan      | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1120 | Julie     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
    And the following shift from '2018-01-18 09:00:00' to '2018-01-18 17:00:00' timestamp '1516284000' and end '1516312800' with duration '8.00' id '55' and plan 'true' available
    And a second shift from '2018-01-19 19:00:00' to '2018-01-19 23:00:00' timestamp '1516406400' and end '1516413600' with duration '4.00' id '56' and plan 'true' available
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | MINIMUM_REST_PERIOD | 1          |
      | 1112 | MINIMUM_REST_PERIOD | 1          |
      | 1113 | MINIMUM_REST_PERIOD | 1          |
      | 1114 | MINIMUM_REST_PERIOD | 1          |
      | 1115 | MINIMUM_REST_PERIOD | 1          |
      | 1116 | MINIMUM_REST_PERIOD | 1          |
      | 1117 | MINIMUM_REST_PERIOD | 1          |
      | 1118 | MINIMUM_REST_PERIOD | 1          |
      | 1119 | MINIMUM_REST_PERIOD | 1          |
      | 1120 | MINIMUM_REST_PERIOD | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 31400       |
      | 1112 | MAYBE                | 4           | 30400         | 31400       |
      | 1113 | MAYBE                | 4           | 30400         | 31400       |
      | 1114 | MAYBE                | 4           | 30400         | 31400       |
      | 1115 | MAYBE                | 4           | 30400         | 31400       |
      | 1116 | MAYBE                | 4           | 30400         | 31400       |
      | 1117 | MAYBE                | 4           | 30400         | 31400       |
      | 1118 | MAYBE                | 4           | 30400         | 31400       |
      | 1119 | MAYBE                | 4           | 30400         | 31400       |
      | 1120 | MAYBE                | 4           | 30400         | 31400       |
    And we apply each employee into the shift for the calculation
    And we apply each employee into the second shift for the calculation
    When Minimum Rest Period rules are calculated
    Then softscore is 1000

  Scenario: testing rule firing for those with exactly their minimum rest period in hours
    Given the following site with id '12' name 'bank' longitude '14.14' and latitude '15.15'
    And with payType 'EMPLOYEE_RATE'
    And the following soft skills
      | id | description |
      | 11 | charming    |
      | 42 | francophone |
    And the following hard skills
      | id | description     |
      | 33 | firearm license |
    And the following employees
      | id   | name      | preferred_hours  | geo_lat  | geo_long  | pay_rate  | seniority  | minimum_rest_period  | skillId |
      | 1111 | Andre     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1112 | Babara    | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1113 | Caspar    | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1114 | Danielle  | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1115 | Eduardo   | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1116 | Franciska | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1117 | Gregory   | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1118 | Helga     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1119 | Ivan      | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1120 | Julie     | 20               | 15       | 14        | 13        | 0          | 8                    | 33      |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
    And the following shift from '2018-01-18 09:00:00' to '2018-01-18 17:00:00' timestamp '1516284000' and end '1516312800' with duration '8.00' id '55' and plan 'true' available
    And a second shift from '2018-01-19 01:00:00' to '2018-01-19 09:00:00' timestamp '1516341600' and end '1516348800' with duration '4.00' id '56' and plan 'true' available
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | MINIMUM_REST_PERIOD | 1          |
      | 1112 | MINIMUM_REST_PERIOD | 1          |
      | 1113 | MINIMUM_REST_PERIOD | 1          |
      | 1114 | MINIMUM_REST_PERIOD | 1          |
      | 1115 | MINIMUM_REST_PERIOD | 1          |
      | 1116 | MINIMUM_REST_PERIOD | 1          |
      | 1117 | MINIMUM_REST_PERIOD | 1          |
      | 1118 | MINIMUM_REST_PERIOD | 1          |
      | 1119 | MINIMUM_REST_PERIOD | 1          |
      | 1120 | MINIMUM_REST_PERIOD | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 31400       |
      | 1112 | MAYBE                | 4           | 30400         | 31400       |
      | 1113 | MAYBE                | 4           | 30400         | 31400       |
      | 1114 | MAYBE                | 4           | 30400         | 31400       |
      | 1115 | MAYBE                | 4           | 30400         | 31400       |
      | 1116 | MAYBE                | 4           | 30400         | 31400       |
      | 1117 | MAYBE                | 4           | 30400         | 31400       |
      | 1118 | MAYBE                | 4           | 30400         | 31400       |
      | 1119 | MAYBE                | 4           | 30400         | 31400       |
      | 1120 | MAYBE                | 4           | 30400         | 31400       |
    And we apply each employee into the shift for the calculation
    And we apply each employee into the second shift for the calculation
    When Minimum Rest Period rules are calculated
    Then softscore is 1000