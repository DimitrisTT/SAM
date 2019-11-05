Feature: More Than Expected Hours

  Background:
    Given More Than Expected Hours being active is 'true'
    And its More Than Expected Hours impact is '-10'
    And it More Than Expected Hours has is Hard Impact set to 'false'

  Scenario: testing rule firing for ten employees expecting more hours
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
      | 1111 | Andre     | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1112 | Babara    | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1113 | Caspar    | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1114 | Danielle  | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1115 | Eduardo   | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1116 | Franciska | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1117 | Gregory   | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1118 | Helga     | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1119 | Ivan      | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1120 | Julie     | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
    And the following shift from '2018-01-18 09:00:00' to '2018-01-18 19:00:00' timestamp '1516284000' and end '1516312800' with duration '8.00' id '55' and plan 'true' available
    And the following employee constraint multipliers
      | id   | name                     | multiplier |
      | 1111 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1112 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1113 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1114 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1115 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1116 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1117 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1118 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1119 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1120 | MORE_THAN_EXPECTED_HOURS | 1          |
    And we apply each employee into the shift for the calculation
    When More Than Expected Hours rules are calculated
    Then softscore is 0

  Scenario: testing rule firing for five employees expecting more hours
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
      | 1111 | Andre     | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1112 | Babara    | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1113 | Caspar    | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1114 | Danielle  | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1115 | Eduardo   | 18               | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1116 | Franciska | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1117 | Gregory   | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1118 | Helga     | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1119 | Ivan      | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1120 | Julie     | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
    And the following shift from '2018-01-18 09:00:00' to '2018-01-18 19:00:00' timestamp '1516284000' and end '1516312800' with duration '8.00' id '55' and plan 'true' available
    And the following employee constraint multipliers
      | id   | name                     | multiplier |
      | 1111 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1112 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1113 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1114 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1115 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1116 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1117 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1118 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1119 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1120 | MORE_THAN_EXPECTED_HOURS | 1          |
    And we apply each employee into the shift for the calculation
    When More Than Expected Hours rules are calculated
    Then softscore is 20000

  Scenario: testing rule firing for 10 employees expecting fewer hours
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
      | 1111 | Andre     | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1112 | Babara    | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1113 | Caspar    | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1114 | Danielle  | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1115 | Eduardo   | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1116 | Franciska | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1117 | Gregory   | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1118 | Helga     | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1119 | Ivan      | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
      | 1120 | Julie     | 2                | 15       | 14        | 13        | 0          | 8                    | 33      |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
    And the following shift from '2018-01-18 09:00:00' to '2018-01-18 19:00:00' timestamp '1516284000' and end '1516312800' with duration '8.00' id '55' and plan 'true' available
    And the following employee constraint multipliers
      | id   | name                     | multiplier |
      | 1111 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1112 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1113 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1114 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1115 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1116 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1117 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1118 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1119 | MORE_THAN_EXPECTED_HOURS | 1          |
      | 1120 | MORE_THAN_EXPECTED_HOURS | 1          |
    And we apply each employee into the shift for the calculation
    When More Than Expected Hours rules are calculated
    Then softscore is 40000
