Feature: PeriodOvertime

  Scenario: No overtime
    Given Payroll start of '2018-02-05' '00:00' in 'America/New_York'
    And pay cycle frequency of weekly
    And hours spanning daily periods will be cut between periods
    And period overtime definitions with id '1' of
      |min|max|type|
      |40 |INF|OT  |
    And holiday periods of
      |start              |end                |
      |2018-01-01 00:00:00|2018-02-01 00:00:00|
    And employee shifts of
      |start              |end                |
      |2018-02-05 09:00:00|2018-02-05 17:00:00|
      |2018-02-06 09:00:00|2018-02-06 17:00:00|
      |2018-02-07 09:00:00|2018-02-07 17:00:00|
      |2018-02-08 09:00:00|2018-02-08 17:00:00|
      |2018-02-09 09:00:00|2018-02-09 17:00:00|
    When overtime is calculated
    Then softscore is 0

  Scenario: No overtime due to hours in previous period
    Given Payroll start of '2018-02-05' '00:00' in 'America/New_York'
    And pay cycle frequency of weekly
    And hours spanning daily periods will be cut between periods
    And period overtime definitions with id '1' of
      |min|max|type|
      |40 |INF|OT  |
    And holiday periods of
      |start              |end                |
      |2018-01-01 00:00:00|2018-02-01 00:00:00|
    And employee shifts of
      |start              |end                |
      |2018-02-04 23:00:00|2018-02-05 08:00:00|
      |2018-02-06 09:00:00|2018-02-06 17:00:00|
      |2018-02-07 09:00:00|2018-02-07 17:00:00|
      |2018-02-08 09:00:00|2018-02-08 17:00:00|
      |2018-02-09 09:00:00|2018-02-09 17:00:00|
    When overtime is calculated
    Then softscore is 0

  Scenario: One hour overtime
    Given Payroll start of '2018-02-05' '00:00' in 'America/New_York'
    And pay cycle frequency of weekly
    And hours spanning daily periods will be cut between periods
    And period overtime definitions with id '1' of
      |min|max|type|
      |40 |INF|OT  |
    And holiday periods of
      |start              |end                |
      |2018-01-01 00:00:00|2018-02-01 00:00:00|
    And employee shifts of
      |start              |end                |
      |2018-02-05 09:00:00|2018-02-05 17:00:00|
      |2018-02-06 09:00:00|2018-02-06 17:00:00|
      |2018-02-07 09:00:00|2018-02-07 17:00:00|
      |2018-02-08 09:00:00|2018-02-08 17:00:00|
      |2018-02-09 09:00:00|2018-02-09 18:00:00|
    When overtime is calculated
    Then softscore is -150

  Scenario: One quarter hour overtime
    Given Payroll start of '2018-02-05' '00:00' in 'America/New_York'
    And pay cycle frequency of weekly
    And hours spanning daily periods will be cut between periods
    And period overtime definitions with id '1' of
      |min|max|type|
      |40 |INF|OT  |
    And holiday periods of
      |start              |end                |
      |2018-01-01 00:00:00|2018-02-01 00:00:00|
    And employee shifts of
      |start              |end                |
      |2018-02-05 09:00:00|2018-02-05 17:00:00|
      |2018-02-06 09:00:00|2018-02-06 17:00:00|
      |2018-02-07 09:00:00|2018-02-07 17:00:00|
      |2018-02-08 09:00:00|2018-02-08 17:00:00|
      |2018-02-09 09:00:00|2018-02-09 17:15:00|
    When overtime is calculated
    Then softscore is -37

  Scenario: 50 hours overtime
    Given Payroll start of '2018-02-05' '00:00' in 'America/New_York'
    And pay cycle frequency of weekly
    And hours spanning daily periods will be cut between periods
    And period overtime definitions with id '1' of
      |min|max|type|
      |40 |INF|OT  |
    And holiday periods of
      |start              |end                |
      |2018-01-01 00:00:00|2018-02-01 00:00:00|
    And employee shifts of
      |start              |end                |
      |2018-02-05 08:00:00|2018-02-05 18:00:00|
      |2018-02-06 08:00:00|2018-02-06 18:00:00|
      |2018-02-07 08:00:00|2018-02-07 18:00:00|
      |2018-02-08 08:00:00|2018-02-08 18:00:00|
      |2018-02-09 08:00:00|2018-02-09 18:00:00|
    When overtime is calculated
    Then softscore is -1500

  Scenario: 50 hours overtime with double time at 50
    Given Payroll start of '2018-02-05' '00:00' in 'America/New_York'
    And pay cycle frequency of weekly
    And hours spanning daily periods will be cut between periods
    And period overtime definitions with id '1' of
      |min|max|type |
      |40 |50 |OT   |
      |50 |INF|DBL  |
    And holiday periods of
      |start              |end                |
      |2018-01-01 00:00:00|2018-02-01 00:00:00|
    And employee shifts of
      |start              |end                |
      |2018-02-05 08:00:00|2018-02-05 18:00:00|
      |2018-02-06 08:00:00|2018-02-06 18:00:00|
      |2018-02-07 08:00:00|2018-02-07 18:00:00|
      |2018-02-08 08:00:00|2018-02-08 18:00:00|
      |2018-02-09 08:00:00|2018-02-09 18:00:00|
    When overtime is calculated
    Then softscore is -1500

  Scenario: 51 hours overtime with double time at 50
    Given Payroll start of '2018-02-05' '00:00' in 'America/New_York'
    And pay cycle frequency of weekly
    And hours spanning daily periods will be cut between periods
    And period overtime definitions with id '1' of
      |min|max|type |
      |40 |50 |OT   |
      |50 |INF|DBL  |
    And holiday periods of
      |start              |end                |
      |2018-01-01 00:00:00|2018-02-01 00:00:00|
    And employee shifts of
      |start              |end                |
      |2018-02-05 08:00:00|2018-02-05 18:00:00|
      |2018-02-06 08:00:00|2018-02-06 18:00:00|
      |2018-02-07 08:00:00|2018-02-07 18:00:00|
      |2018-02-08 08:00:00|2018-02-08 18:00:00|
      |2018-02-09 08:00:00|2018-02-09 19:00:00|
    When overtime is calculated
    Then softscore is -1650
