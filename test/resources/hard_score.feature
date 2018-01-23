Feature: Hard constraints

  Scenario: Shifts overlap
    Given Employee has id 1
    And he has shift from <start> to <end>
    And shift from now to then
    When scheduling
    Then hardscore is -1

  Examples:
  | id |  start   |     end | hardscore |
  |    1 |  26500 |     215 | -1 |
  |    2 |  29500 |     245 | 0 |
  |    3 |  31500 |     255 | 0 |
  |    3 |  37000 |     305 | 0 |