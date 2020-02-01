package com.tracktik.scheduler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * This is a class to hold the Employee Site Distance
 * usually run at ruletime for the distance method
 *
 * Methods imported by lombok:
 * Data
 * Accessors
 * Constructors
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSiteDistance {

  private String employeeId;
  private String siteId;
  private Long distance;

  /*
   * This is a method to calculate the distance between two points, usually between an employee and a site
   * @param geo1_latitude the latitude or y axis value of the first point
   * @param geo1_longitude the longitude or x axis value of the first point
   * @param geo2_latitude the latitude or y axis value of the second point
   * @param geo2_longitude the longitude or x axis value of the second point
   *
   */
  public static Long distance(Double geo1_latitude, Double geo1_longitude, Double geo2_latitude, Double geo2_longitude) {
    Double distance = (Math.acos(Math.sin(geo1_latitude * Math.PI / 180D) * Math.sin(geo2_latitude * Math.PI / 180D) + Math.cos(geo1_latitude * Math.PI / 180D) * Math.cos(geo2_latitude * Math.PI / 180D) * Math.cos((geo1_longitude - geo2_longitude) * Math.PI / 180D)) * 180 / Math.PI) * 60 * 1.1515D;
    return distance.longValue();
  }
}
