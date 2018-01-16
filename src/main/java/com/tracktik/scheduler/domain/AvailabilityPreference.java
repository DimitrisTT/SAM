package com.tracktik.scheduler.domain;

public class AvailabilityPreference {
	
	private Integer requestedHoursPerWeek;

	public Integer getRequestedHoursPerWeek() {
		return requestedHoursPerWeek;
	}

	public void setRequestedHoursPerWeek(Integer requestedHoursPerWeek) {
		this.requestedHoursPerWeek = requestedHoursPerWeek;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestedHoursPerWeek == null) ? 0 : requestedHoursPerWeek.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvailabilityPreference other = (AvailabilityPreference) obj;
		if (requestedHoursPerWeek == null) {
			if (other.requestedHoursPerWeek != null)
				return false;
		} else if (!requestedHoursPerWeek.equals(other.requestedHoursPerWeek))
			return false;
		return true;
	}
}
