Feature: Overtime Track Tik tests

#  Scenario: Shift and period functionality
#    Given Payroll start of '2020-01-01' '00:00' in 'America/Vancouver'
#    And pay cycle frequency of weekly
#    And hours spanning daily periods will be cut between periods
#    And count holiday hours towards period overtime
#    And employee shifts of
#      |start              |end                |
#      |2020-01-02 04:00:00|2020-01-02 09:00:00|
#      |2020-01-07 22:00:00|2020-01-08 02:00:00|
#    When overtime is calculated
#    Then Payroll results are as expected
#      | id | hour | minute | second | totHours | timestampDifference | payrollType |
#      | 0  | 7    | 0      | 0      | 7        | 25200               | REG         |
#      | 1  | 2    | 0      | 0      | 2        | 7200                | REG         |

Scenario:
  Given employee shifts of
    |start              |end                |
    |2020-01-02 04:00:00|2020-01-02 09:00:00|
    |2020-01-07 22:00:00|2020-01-08 02:00:00|
  When overtime is calculated
  Then a ShiftMinimum of 2020-01-02 04:00:00 is expected
  Then a ShiftMaximum of 2020-01-08 02:00:00 is expected

  Scenario:
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

#  Scenario:
#    Given employee shifts of
#      |start              |end                |
#      |2020-01-02 04:00:00|2020-01-02 09:00:00|
#      |2020-01-07 22:00:00|2020-01-08 02:00:00|
#    And a PayrollSchedule of
#      |frequency|periodStartTime|periodStartDate|
#      | weekly  | 00:00         | 2020-01-01    |
#    When overtime is calculated
#    Then the following PayrollPeriods are expected
#      |start              |end                |
#      |2020-01-01 00:00:00|2020-01-08 00:00:00|
#      |2020-01-08 00:00:00|2020-01-15 02:00:00|