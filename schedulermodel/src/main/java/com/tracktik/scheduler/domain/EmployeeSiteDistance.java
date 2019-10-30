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

  public static Long distance(Double geo1_latitude, Double geo1_longitude, Double geo2_latitude, Double geo2_longitude) {
    Double distance = (Math.acos(Math.sin(geo1_latitude * Math.PI / 180D) * Math.sin(geo2_latitude * Math.PI / 180D) + Math.cos(geo1_latitude * Math.PI / 180D) * Math.cos(geo2_latitude * Math.PI / 180D) * Math.cos((geo1_longitude - geo2_longitude) * Math.PI / 180D)) * 180 / Math.PI) * 60 * 1.1515D;
    return distance.longValue();
  }
}
