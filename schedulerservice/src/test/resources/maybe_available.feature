Feature: Maybe Available

  Background:
    Given Maybe Available being active is 'true'
    And its Maybe Available score impact is '-100'

  Scenario: testing rule firing for those with no overlap in time
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
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
    When Maybe Available rules are calculated
    Then softscore is 0

  Scenario: testing rule firing for those with overlap in the beginning
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 34400       |
      | 1112 | MAYBE                | 4           | 30400         | 34400       |
      | 1113 | MAYBE                | 4           | 30400         | 34400       |
      | 1114 | MAYBE                | 4           | 30400         | 34400       |
      | 1115 | MAYBE                | 4           | 30400         | 34400       |
      | 1116 | MAYBE                | 4           | 30400         | 34400       |
      | 1117 | MAYBE                | 4           | 30400         | 34400       |
      | 1118 | MAYBE                | 4           | 30400         | 34400       |
      | 1119 | MAYBE                | 4           | 30400         | 34400       |
      | 1120 | MAYBE                | 4           | 30400         | 34400       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -1000

  Scenario: testing rule firing for those with overlap in the end
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 60200         | 62200       |
      | 1112 | MAYBE                | 4           | 60200         | 62200       |
      | 1113 | MAYBE                | 4           | 60200         | 62200       |
      | 1114 | MAYBE                | 4           | 60200         | 62200       |
      | 1115 | MAYBE                | 4           | 60200         | 62200       |
      | 1116 | MAYBE                | 4           | 60200         | 62200       |
      | 1117 | MAYBE                | 4           | 60200         | 62200       |
      | 1118 | MAYBE                | 4           | 60200         | 62200       |
      | 1119 | MAYBE                | 4           | 60200         | 62200       |
      | 1120 | MAYBE                | 4           | 60200         | 62200       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -1000

  Scenario: testing rule firing for those with overlap in the entirety
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 62200       |
      | 1112 | MAYBE                | 4           | 30400         | 62200       |
      | 1113 | MAYBE                | 4           | 30400         | 62200       |
      | 1114 | MAYBE                | 4           | 30400         | 62200       |
      | 1115 | MAYBE                | 4           | 30400         | 62200       |
      | 1116 | MAYBE                | 4           | 30400         | 62200       |
      | 1117 | MAYBE                | 4           | 30400         | 62200       |
      | 1118 | MAYBE                | 4           | 30400         | 62200       |
      | 1119 | MAYBE                | 4           | 30400         | 62200       |
      | 1120 | MAYBE                | 4           | 30400         | 62200       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -1000

  Scenario: testing rule firing for half of those with overlap in the entirety and half in the beginning
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 34400       |
      | 1112 | MAYBE                | 4           | 30400         | 34400       |
      | 1113 | MAYBE                | 4           | 30400         | 34400       |
      | 1114 | MAYBE                | 4           | 30400         | 34400       |
      | 1115 | MAYBE                | 4           | 30400         | 34400       |
      | 1116 | MAYBE                | 4           | 30400         | 62200       |
      | 1117 | MAYBE                | 4           | 30400         | 62200       |
      | 1118 | MAYBE                | 4           | 30400         | 62200       |
      | 1119 | MAYBE                | 4           | 30400         | 62200       |
      | 1120 | MAYBE                | 4           | 30400         | 62200       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -1000


  Scenario: testing rule firing for half of those with overlap in the entirety and half in the end
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 62200       |
      | 1112 | MAYBE                | 4           | 30400         | 62200       |
      | 1113 | MAYBE                | 4           | 30400         | 62200       |
      | 1114 | MAYBE                | 4           | 30400         | 62200       |
      | 1115 | MAYBE                | 4           | 30400         | 62200       |
      | 1116 | MAYBE                | 4           | 60200         | 62200       |
      | 1117 | MAYBE                | 4           | 60200         | 62200       |
      | 1118 | MAYBE                | 4           | 60200         | 62200       |
      | 1119 | MAYBE                | 4           | 60200         | 62200       |
      | 1120 | MAYBE                | 4           | 60200         | 62200       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -1000

  Scenario: testing rule firing for half of those with no overlap and half overlapping in the entirety
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 31400       |
      | 1112 | MAYBE                | 4           | 30400         | 31400       |
      | 1113 | MAYBE                | 4           | 30400         | 31400       |
      | 1114 | MAYBE                | 4           | 30400         | 31400       |
      | 1115 | MAYBE                | 4           | 30400         | 31400       |
      | 1116 | MAYBE                | 4           | 30400         | 62200       |
      | 1117 | MAYBE                | 4           | 30400         | 62200       |
      | 1118 | MAYBE                | 4           | 30400         | 62200       |
      | 1119 | MAYBE                | 4           | 30400         | 62200       |
      | 1120 | MAYBE                | 4           | 30400         | 62200       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -500


  Scenario: testing rule firing for half of those with overlap in the beginning and half with no overlap
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 34400       |
      | 1112 | MAYBE                | 4           | 30400         | 34400       |
      | 1113 | MAYBE                | 4           | 30400         | 34400       |
      | 1114 | MAYBE                | 4           | 30400         | 34400       |
      | 1115 | MAYBE                | 4           | 30400         | 34400       |
      | 1116 | MAYBE                | 4           | 30400         | 31400       |
      | 1117 | MAYBE                | 4           | 30400         | 31400       |
      | 1118 | MAYBE                | 4           | 30400         | 31400       |
      | 1119 | MAYBE                | 4           | 30400         | 31400       |
      | 1120 | MAYBE                | 4           | 30400         | 31400       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -500



  Scenario: testing rule firing for half of those with overlap in the end and half in the beginning
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 34400       |
      | 1112 | MAYBE                | 4           | 30400         | 34400       |
      | 1113 | MAYBE                | 4           | 30400         | 34400       |
      | 1114 | MAYBE                | 4           | 30400         | 34400       |
      | 1115 | MAYBE                | 4           | 30400         | 34400       |
      | 1116 | MAYBE                | 4           | 60200         | 62200       |
      | 1117 | MAYBE                | 4           | 60200         | 62200       |
      | 1118 | MAYBE                | 4           | 60200         | 62200       |
      | 1119 | MAYBE                | 4           | 60200         | 62200       |
      | 1120 | MAYBE                | 4           | 60200         | 62200       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -1000

  Scenario: testing rule firing for half of those with no overlap and half with an overlap in the end
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
    And the following employee constraint multipliers
      | id   | name          | multiplier |
      | 1111 | AVAILABILITY_MAYBE | 1          |
      | 1112 | AVAILABILITY_MAYBE | 1          |
      | 1113 | AVAILABILITY_MAYBE | 1          |
      | 1114 | AVAILABILITY_MAYBE | 1          |
      | 1115 | AVAILABILITY_MAYBE | 1          |
      | 1116 | AVAILABILITY_MAYBE | 1          |
      | 1117 | AVAILABILITY_MAYBE | 1          |
      | 1118 | AVAILABILITY_MAYBE | 1          |
      | 1119 | AVAILABILITY_MAYBE | 1          |
      | 1120 | AVAILABILITY_MAYBE | 1          |
    And the following employee availabilities
      | id   | availability_type | day_of_week | seconds_start | seconds_end |
      | 1111 | MAYBE                | 4           | 30400         | 31400       |
      | 1112 | MAYBE                | 4           | 30400         | 31400       |
      | 1113 | MAYBE                | 4           | 30400         | 31400       |
      | 1114 | MAYBE                | 4           | 30400         | 31400       |
      | 1115 | MAYBE                | 4           | 30400         | 31400       |
      | 1116 | MAYBE                | 4           | 60400         | 62200       |
      | 1117 | MAYBE                | 4           | 60400         | 62200       |
      | 1118 | MAYBE                | 4           | 60400         | 62200       |
      | 1119 | MAYBE                | 4           | 60400         | 62200       |
      | 1120 | MAYBE                | 4           | 60400         | 62200       |
    And we apply each employee into the shift for the calculation
    When Maybe Available rules are calculated
    Then softscore is -500
    