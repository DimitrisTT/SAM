Feature: Hard Skill Missing

  Background:
    Given Hard Skill Missing being active is 'true'
    And its Hard Skill Missing is Hard Failure is set to 'true'
    And its Hard Skill Missing impact multiplier is '5'
    And its Hard Skill Missing is Hard Impact set to 'false'

  Scenario: testing rule firing for those all without the hard skill necessary
    Given the following site with id '12' name 'bank' longitude '14.14' and latitude '15.15'
    And with payType 'EMPLOYEE_RATE'
    And the following soft skills
      | id | description |
      | 11 | charming    |
    And the following hard skills
      | id | description     |
      | 33 | firearm license |
      | 42 | francophone     |
    And the following employees
      | id   | name      | preferred_hours  | geo_lat  | geo_long  | pay_rate  | seniority  | minimum_rest_period  | skillId | secondSkillId |
      | 1111 | Andre     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1112 | Babara    | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1113 | Caspar    | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1114 | Danielle  | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1115 | Eduardo   | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1116 | Franciska | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1117 | Gregory   | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1118 | Helga     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1119 | Ivan      | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1120 | Julie     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
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
    When Hard Skill Missing rules are calculated
    Then hardscore is -10

  Scenario: testing rule firing for half of those without the hard skill necessary
    Given the following site with id '12' name 'bank' longitude '14.14' and latitude '15.15'
    And with payType 'EMPLOYEE_RATE'
    And the following soft skills
      | id | description |
      | 11 | charming    |
    And the following hard skills
      | id | description     |
      | 33 | firearm license |
      | 42 | francophone     |
    And the following employees
      | id   | name      | preferred_hours  | geo_lat  | geo_long  | pay_rate  | seniority  | minimum_rest_period  | skillId | secondSkillId |
      | 1111 | Andre     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1112 | Babara    | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1113 | Caspar    | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1114 | Danielle  | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1115 | Eduardo   | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 11            |
      | 1116 | Franciska | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1117 | Gregory   | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1118 | Helga     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1119 | Ivan      | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1120 | Julie     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
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
    When Hard Skill Missing rules are calculated
    Then hardscore is -5

  Scenario: testing rule firing for those all with the hard skill necessary
    Given the following site with id '12' name 'bank' longitude '14.14' and latitude '15.15'
    And with payType 'EMPLOYEE_RATE'
    And the following soft skills
      | id | description |
      | 11 | charming    |
    And the following hard skills
      | id | description     |
      | 33 | firearm license |
      | 42 | francophone     |
    And the following employees
      | id   | name      | preferred_hours  | geo_lat  | geo_long  | pay_rate  | seniority  | minimum_rest_period  | skillId | secondSkillId |
      | 1111 | Andre     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1112 | Babara    | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1113 | Caspar    | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1114 | Danielle  | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1115 | Eduardo   | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1116 | Franciska | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1117 | Gregory   | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1118 | Helga     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1119 | Ivan      | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
      | 1120 | Julie     | 20               | 15       | 14        | 13        | 0          | 8                    | 42      | 33            |
    And the following tags
      | tag      |
      | sometag  |
      | othertag |
    And the following post with id '1337' name 'armed guard' bill rate '12' and pay rate '20'
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
    When Hard Skill Missing rules are calculated
    Then hardscore is 0
