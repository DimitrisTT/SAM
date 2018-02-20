package com.tracktik.scheduler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSiteDistance {

  private String employeeId;
  private String siteId;
  private Long distance;

}
