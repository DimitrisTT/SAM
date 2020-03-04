Feature: Overtime Track Tik tests

  Scenario: testing shift max and min with one of each
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-08 02:00:00|
    When overtime is calculated
    Then a ShiftMinimum of 2020-01-02 04:00:00 is expected
    Then a ShiftMaximum of 2020-01-08 02:00:00 is expected

  Scenario: testing shift max and min with multiple of each
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-02 04:00:00|2020-01-02 10:00:00|
      |2020-01-02 04:00:00|2020-01-08 02:00:00|
      |2020-01-07 21:00:00|2020-01-08 02:00:00|
      |2020-01-07 22:00:00|2020-01-08 02:00:00|
    When overtime is calculated
    Then a ShiftMinimum of 2020-01-02 04:00:00 is expected
    Then a ShiftMaximum of 2020-01-08 02:00:00 is expected

  Scenario: testing weekly creation of PayPeriods by shifts
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-08 02:00:00|
      |2020-01-09 10:00:00|2020-01-09 22:00:00|
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    When overtime is calculated
    Then the following PayrollPeriodStarts are expected
      |start              |
      |2020-01-01 00:00:00|
      |2020-01-08 00:00:00|
    Then the following PayrollPeriodEnds are expected
      |end                |
      |2020-01-08 00:00:00|
      |2020-01-15 00:00:00|

  Scenario: testing bi_weekly creation of PayPeriods by shifts
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-08 02:00:00|
      |2020-01-09 10:00:00|2020-01-09 22:00:00|
    And a PayrollSchedule of
      |frequency  |periodStartTime|periodStartDate|
      | bi_weekly | 00:00         | 2020-01-01    |
    When overtime is calculated
    Then the following PayrollPeriodStarts are expected
      |start              |
      |2020-01-01 00:00:00|
    Then the following PayrollPeriodEnds are expected
      |end                |
      |2020-01-15 00:00:00|

  Scenario: initializing workdays weekly with holidays
    Given employee shifts of
      |start              |end                |
      |2020-02-02 04:00:00|2020-02-02 09:00:00|
      |2020-02-07 22:00:00|2020-02-07 23:00:00|
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-02-01    |
    And count holiday hours towards period overtime
    And holiday periods of
      |start              |end                |
      |2020-02-02 00:00:00|2020-02-03 00:00:00|
      |2020-02-05 00:00:00|2020-02-06 00:00:00|
    When overtime is calculated
    Then the following WorkDays with holidays are expected
      | id | start          | end            | holidayFlag |
      | 0  |2020-02-01T00:00|2020-02-02T00:00| false       |
      | 1  |2020-02-02T00:00|2020-02-03T00:00| true        |
      | 2  |2020-02-03T00:00|2020-02-04T00:00| false       |
      | 3  |2020-02-04T00:00|2020-02-05T00:00| false       |
      | 4  |2020-02-05T00:00|2020-02-06T00:00| true        |
      | 5  |2020-02-06T00:00|2020-02-07T00:00| false       |
      | 6  |2020-02-07T00:00|2020-02-08T00:00| false       |

  Scenario: initializing workdays bi_weekly with holidays
    Given employee shifts of
      |start              |end                |
      |2020-02-02 04:00:00|2020-02-02 09:00:00|
      |2020-02-07 22:00:00|2020-02-07 23:00:00|
    And a PayrollSchedule of
      |frequency   |periodStartTime|periodStartDate|
      | bi_weekly  | 00:00         | 2020-02-01    |
    And count holiday hours towards period overtime
    And holiday periods of
      |start              |end                |
      |2020-02-02 00:00:00|2020-02-03 00:00:00|
      |2020-02-05 00:00:00|2020-02-06 00:00:00|
    When overtime is calculated
    Then the following WorkDays with holidays are expected
      | id | start          | end            | holidayFlag |
      | 0  |2020-02-01T00:00|2020-02-02T00:00| false       |
      | 1  |2020-02-02T00:00|2020-02-03T00:00| true        |
      | 2  |2020-02-03T00:00|2020-02-04T00:00| false       |
      | 3  |2020-02-04T00:00|2020-02-05T00:00| false       |
      | 4  |2020-02-05T00:00|2020-02-06T00:00| true        |
      | 5  |2020-02-06T00:00|2020-02-07T00:00| false       |
      | 6  |2020-02-07T00:00|2020-02-08T00:00| false       |
      | 7  |2020-02-08T00:00|2020-02-09T00:00| false       |
      | 8  |2020-02-09T00:00|2020-02-10T00:00| false       |
      | 9  |2020-02-10T00:00|2020-02-11T00:00| false       |
      | 10 |2020-02-11T00:00|2020-02-12T00:00| false       |
      | 11 |2020-02-12T00:00|2020-02-13T00:00| false       |
      | 12 |2020-02-13T00:00|2020-02-14T00:00| false       |
      | 13 |2020-02-14T00:00|2020-02-15T00:00| false       |

  Scenario: initializing workdays weekly
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-07 23:00:00|
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    When overtime is calculated
    Then the following WorkDays are expected
      | id | start          | end            |
      | 0  |2020-01-01T00:00|2020-01-02T00:00|
      | 1  |2020-01-02T00:00|2020-01-03T00:00|
      | 2  |2020-01-03T00:00|2020-01-04T00:00|
      | 3  |2020-01-04T00:00|2020-01-05T00:00|
      | 4  |2020-01-05T00:00|2020-01-06T00:00|
      | 5  |2020-01-06T00:00|2020-01-07T00:00|
      | 6  |2020-01-07T00:00|2020-01-08T00:00|

  Scenario: initializing workdays bi_weekly
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-07 23:00:00|
    And a PayrollSchedule of
      |frequency   |periodStartTime|periodStartDate|
      | bi_weekly  | 00:00         | 2020-01-01    |
    When overtime is calculated
    Then the following WorkDays are expected
      | id | start          | end            |
      | 0  |2020-01-01T00:00|2020-01-02T00:00|
      | 1  |2020-01-02T00:00|2020-01-03T00:00|
      | 2  |2020-01-03T00:00|2020-01-04T00:00|
      | 3  |2020-01-04T00:00|2020-01-05T00:00|
      | 4  |2020-01-05T00:00|2020-01-06T00:00|
      | 5  |2020-01-06T00:00|2020-01-07T00:00|
      | 6  |2020-01-07T00:00|2020-01-08T00:00|
      | 7  |2020-01-08T00:00|2020-01-09T00:00|
      | 8  |2020-01-09T00:00|2020-01-10T00:00|
      | 9  |2020-01-10T00:00|2020-01-11T00:00|
      | 10 |2020-01-11T00:00|2020-01-12T00:00|
      | 11 |2020-01-12T00:00|2020-01-13T00:00|
      | 12 |2020-01-13T00:00|2020-01-14T00:00|
      | 13 |2020-01-14T00:00|2020-01-15T00:00|

  Scenario: Testing Workslices with cut
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-07 23:00:00|
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then we expect the following workslices
      | employeeId | workDayId | workDayStart    | workDayEnd     | start               | end                 | payrollType |
      | 1          | 1         | 2020-01-02T00:00|2020-01-03T00:00| 2020-01-02 04:00:00 | 2020-01-02 09:00:00 | REG         |
      | 1          | 6         | 2020-01-07T00:00|2020-01-08T00:00| 2020-01-07 22:00:00 | 2020-01-07 23:00:00 | REG         |

  Scenario: Testing Workslices with span
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-07 23:00:00|
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods
    When overtime is calculated
    Then we expect the following workslices
      | employeeId | workDayId | workDayStart    | workDayEnd     | start               | end                 | payrollType |
      | 1          | 1         | 2020-01-02T00:00|2020-01-03T00:00| 2020-01-02 04:00:00 | 2020-01-02 09:00:00 | REG         |
      | 1          | 6         | 2020-01-07T00:00|2020-01-08T00:00| 2020-01-07 22:00:00 | 2020-01-07 23:00:00 | REG         |

  Scenario: Day Overtime
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 18:00:00|2020-01-07 23:00:00|
    And daily overtime definitions with id '1' of
      | type | min | max |
      | OT   | 4   | 10  |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then softscore is -30

  Scenario: Period Overtime With Holidays
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 18:00:00|2020-01-07 23:00:00|
    And period overtime definitions with id '1' of
      | type | min | max |
      | OT   | 4   | 10  |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    And count holiday hours towards period overtime
    And holiday periods of
      |start              |end                |
      |2020-01-02 00:00:00|2020-01-03 00:00:00|
      |2020-01-05 00:00:00|2020-01-06 00:00:00|
    When overtime is calculated
    Then softscore is -90

  Scenario: Period Overtime
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 09:00:00|
      |2020-01-07 22:00:00|2020-01-07 23:00:00|
    And period overtime definitions with id '1' of
      | type | min | max |
      | OT   | 4   | 10  |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then softscore is -30

  Scenario: Testing Workslices with cut
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 14:00:00|
      |2020-01-03 04:00:00|2020-01-03 14:00:00|
      |2020-01-04 04:00:00|2020-01-04 14:00:00|
      |2020-01-05 04:00:00|2020-01-05 14:00:00|
      |2020-01-06 04:00:00|2020-01-06 14:00:00|
      |2020-01-07 04:00:00|2020-01-07 14:00:00|
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then for employee with id '1' we expect start '2020-01-02' and end '2020-01-07'

  Scenario: Testing Consecutive Workdays OT only
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 14:00:00|
      |2020-01-03 04:00:00|2020-01-03 14:00:00|
      |2020-01-04 04:00:00|2020-01-04 14:00:00|
      |2020-01-05 04:00:00|2020-01-05 14:00:00|
      |2020-01-06 04:00:00|2020-01-06 14:00:00|
      |2020-01-07 04:00:00|2020-01-07 14:00:00|
    And consecutive day overtime definitions with id '1' of
      |minDays|maxDays|minHours|maxHours|type|
      |5      |INF    |0       |8       |OT  |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then softscore is -120

  Scenario: Testing Consecutive Workdays DBL only
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 14:00:00|
      |2020-01-03 04:00:00|2020-01-03 14:00:00|
      |2020-01-04 04:00:00|2020-01-04 14:00:00|
      |2020-01-05 04:00:00|2020-01-05 14:00:00|
      |2020-01-06 04:00:00|2020-01-06 14:00:00|
      |2020-01-07 04:00:00|2020-01-07 14:00:00|
    And consecutive day overtime definitions with id '1' of
      |minDays|maxDays|minHours|maxHours|type|
      |5      |INF    |0       |8       |DBL |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then softscore is -160

  Scenario: Testing Consecutive Workdays both OT and DBL (above total)
    Given employee shifts of
      |start              |end                |
      |2020-01-01 04:00:00|2020-01-01 14:00:00|
      |2020-01-02 04:00:00|2020-01-02 14:00:00|
      |2020-01-03 04:00:00|2020-01-03 14:00:00|
      |2020-01-04 04:00:00|2020-01-04 14:00:00|
      |2020-01-05 04:00:00|2020-01-05 14:00:00|
      |2020-01-06 04:00:00|2020-01-06 14:00:00|
      |2020-01-07 04:00:00|2020-01-07 14:00:00|
    And consecutive day overtime definitions with id '1' of
      |minDays|maxDays|minHours|maxHours|type|
      |5      |INF    |0       |8       |OT  |
      |5      |INF    |8       |12      |DBL |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then softscore is -200

  Scenario: Testing Consecutive Workdays both OT and DBL (all OT, some DBL)
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 14:00:00|
      |2020-01-03 04:00:00|2020-01-03 14:00:00|
      |2020-01-04 04:00:00|2020-01-04 14:00:00|
      |2020-01-05 04:00:00|2020-01-05 14:00:00|
      |2020-01-06 04:00:00|2020-01-06 14:00:00|
      |2020-01-07 04:00:00|2020-01-07 14:00:00|
    And consecutive day overtime definitions with id '1' of
      |minDays|maxDays|minHours|maxHours|type|
      |5      |INF    |0       |8       |OT  |
      |5      |INF    |8       |INF     |DBL |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then softscore is -160

  Scenario: Testing Consecutive Workdays both OT and DBL (some OT)
    Given employee shifts of
      |start              |end                |
      |2020-01-02 04:00:00|2020-01-02 14:00:00|
      |2020-01-03 04:00:00|2020-01-03 14:00:00|
      |2020-01-04 04:00:00|2020-01-04 14:00:00|
      |2020-01-05 04:00:00|2020-01-05 14:00:00|
      |2020-01-06 04:00:00|2020-01-06 14:00:00|
      |2020-01-07 04:00:00|2020-01-07 09:00:00|
    And consecutive day overtime definitions with id '1' of
      |minDays|maxDays|minHours|maxHours|type|
      |5      |INF    |0       |8       |OT  |
      |5      |INF    |8       |INF     |DBL |
    And a PayrollSchedule of
      |frequency|periodStartTime|periodStartDate|
      | weekly  | 00:00         | 2020-01-01    |
    And hours spanning daily periods will be cut between periods
    When overtime is calculated
    Then softscore is -75