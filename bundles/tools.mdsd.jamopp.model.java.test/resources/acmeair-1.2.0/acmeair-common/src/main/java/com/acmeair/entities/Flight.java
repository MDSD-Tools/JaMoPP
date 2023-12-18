/*******************************************************************************
* Copyright (c) 2013 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
package com.acmeair.entities;

import java.util.*;
import java.io.Serializable;
import java.math.BigDecimal;

public class Flight implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private FlightPK _id;
	
	private FlightPK pkey;
	private Date scheduledDepartureTime;
	private Date scheduledArrivalTime;
	private BigDecimal firstClassBaseCost;
	private BigDecimal economyClassBaseCost;
	private int numFirstClassSeats;
	private int numEconomyClassSeats;
	private String airplaneTypeId;
	
	private FlightSegment flightSegment;
	
	public Flight() {
	}
	
	public Flight(String id, String flightSegmentId,
			Date scheduledDepartureTime, Date scheduledArrivalTime,
			BigDecimal firstClassBaseCost, BigDecimal economyClassBaseCost,
			int numFirstClassSeats, int numEconomyClassSeats,
			String airplaneTypeId) {
		this.pkey = new FlightPK(flightSegmentId,id);
		this._id = this.pkey;
		this.scheduledDepartureTime = scheduledDepartureTime;
		this.scheduledArrivalTime = scheduledArrivalTime;
		this.firstClassBaseCost = firstClassBaseCost;
		this.economyClassBaseCost = economyClassBaseCost;
		this.numFirstClassSeats = numFirstClassSeats;
		this.numEconomyClassSeats = numEconomyClassSeats;
		this.airplaneTypeId = airplaneTypeId;
	}

	public FlightPK getPkey() {
		return pkey;
	}

	public void setPkey(FlightPK pkey) {
		this.pkey = pkey;
	}

	// The method is needed for index calculation
	public String getFlightSegmentId()
	{
		return pkey.getFlightSegmentId();
	}
	
	public Date getScheduledDepartureTime() {
		return scheduledDepartureTime;
	}


	public void setScheduledDepartureTime(Date scheduledDepartureTime) {
		this.scheduledDepartureTime = scheduledDepartureTime;
	}


	public Date getScheduledArrivalTime() {
		return scheduledArrivalTime;
	}


	public void setScheduledArrivalTime(Date scheduledArrivalTime) {
		this.scheduledArrivalTime = scheduledArrivalTime;
	}


	public BigDecimal getFirstClassBaseCost() {
		return firstClassBaseCost;
	}


	public void setFirstClassBaseCost(BigDecimal firstClassBaseCost) {
		this.firstClassBaseCost = firstClassBaseCost;
	}


	public BigDecimal getEconomyClassBaseCost() {
		return economyClassBaseCost;
	}


	public void setEconomyClassBaseCost(BigDecimal economyClassBaseCost) {
		this.economyClassBaseCost = economyClassBaseCost;
	}


	public int getNumFirstClassSeats() {
		return numFirstClassSeats;
	}


	public void setNumFirstClassSeats(int numFirstClassSeats) {
		this.numFirstClassSeats = numFirstClassSeats;
	}


	public int getNumEconomyClassSeats() {
		return numEconomyClassSeats;
	}


	public void setNumEconomyClassSeats(int numEconomyClassSeats) {
		this.numEconomyClassSeats = numEconomyClassSeats;
	}


	public String getAirplaneTypeId() {
		return airplaneTypeId;
	}


	public void setAirplaneTypeId(String airplaneTypeId) {
		this.airplaneTypeId = airplaneTypeId;
	}


	public FlightSegment getFlightSegment() {
		return flightSegment;
	}

	public void setFlightSegment(FlightSegment flightSegment) {
		this.flightSegment = flightSegment;
	}

	@Override
	public String toString() {
		return "Flight key="+pkey
				+ ", scheduledDepartureTime=" + scheduledDepartureTime
				+ ", scheduledArrivalTime=" + scheduledArrivalTime
				+ ", firstClassBaseCost=" + firstClassBaseCost
				+ ", economyClassBaseCost=" + economyClassBaseCost
				+ ", numFirstClassSeats=" + numFirstClassSeats
				+ ", numEconomyClassSeats=" + numEconomyClassSeats
				+ ", airplaneTypeId=" + airplaneTypeId + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		if (airplaneTypeId == null) {
			if (other.airplaneTypeId != null)
				return false;
		} else if (!airplaneTypeId.equals(other.airplaneTypeId))
			return false;
		if (economyClassBaseCost == null) {
			if (other.economyClassBaseCost != null)
				return false;
		} else if (!economyClassBaseCost.equals(other.economyClassBaseCost))
			return false;
		if (firstClassBaseCost == null) {
			if (other.firstClassBaseCost != null)
				return false;
		} else if (!firstClassBaseCost.equals(other.firstClassBaseCost))
			return false;
		if (flightSegment == null) {
			if (other.flightSegment != null)
				return false;
		} else if (!flightSegment.equals(other.flightSegment))
			return false;
		if (pkey == null) {
			if (other.pkey != null)
				return false;
		} else if (!pkey.equals(other.pkey))
			return false;
		if (numEconomyClassSeats != other.numEconomyClassSeats)
			return false;
		if (numFirstClassSeats != other.numFirstClassSeats)
			return false;
		if (scheduledArrivalTime == null) {
			if (other.scheduledArrivalTime != null)
				return false;
		} else if (!scheduledArrivalTime.equals(other.scheduledArrivalTime))
			return false;
		if (scheduledDepartureTime == null) {
			if (other.scheduledDepartureTime != null)
				return false;
		} else if (!scheduledDepartureTime.equals(other.scheduledDepartureTime))
			return false;
		return true;
	}
	
	
}