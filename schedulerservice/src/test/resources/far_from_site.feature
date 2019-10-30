Feature: Far From Site

  Background:
    Given Far From Site being active is 'true'
    And its definition is set to '100'
    And its score impact is '-70'
    And it has is Hard Impact set to 'true'

  Scenario: testing rule firing for those far away
    Given the following site with id '12' name 'bank' longitude '14.14' and latitude '15.15'
    And the following employees
    | id   | name      | preferred_hours  | geo_lat  | geo_long  | pay_rate  | seniority  | minimum_rest_period  |
    | 1111 | Andre     | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1112 | Babara    | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1113 | Caspar    | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1114 | Danielle  | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1115 | Eduardo   | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1116 | Franciska | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1117 | Gregory   | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1118 | Helga     | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1119 | Ivan      | 20               | 15       | 14        | 13        | 120        | 8                    |
    | 1120 | Julie     | 20               | 15       | 14        | 13        | 120        | 8                    |
    And with payType 'EMPLOYEE_RATE'
    And the following soft skills
    | id | description |
    | 11 | charming    |
    | 42 | awareness   |
    And the following hard skills
    | id | description     |
    | 33 | firearm license |
    | 27 | francophone     |
    And the following tags
    | tag      |
    | sometag  |
    | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12.0' and pay rate '20.0'
    And the following shift from '2018-01-18 09:00:00' to '2018-01-18 17:00:00' timestamp '1516284000' and end '1516312800' with duration '8.00' id '55' and plan 'true' available
    And the following employee constraint multipliers
    | id   | name          | multiplier |
    | 1111 | WORKPLACE_FAR | 1          |
    | 1112 | WORKPLACE_FAR | 1          |
    | 1113 | WORKPLACE_FAR | 1          |
    | 1114 | WORKPLACE_FAR | 1          |
    | 1115 | WORKPLACE_FAR | 1          |
    | 1116 | WORKPLACE_FAR | 1          |
    | 1117 | WORKPLACE_FAR | 1          |
    | 1118 | WORKPLACE_FAR | 1          |
    | 1119 | WORKPLACE_FAR | 1          |
    | 1120 | WORKPLACE_FAR | 1          |
    And we apply each employee into the shift for the calculation
    When Far From Site rules are calculated
    Then softscore is 0
