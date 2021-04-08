package com.kylevanfleet.babysitter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class NightlyPayCalculator {
	
	private double preBedRate = 12.00;
	private double bedToMidnightRate = 8.00;
	private double postMidnightRate = 16.00;
	
	private final LocalTime MIDNIGHT = LocalTime.parse("12:00AM", DateTimeFormatter.ofPattern("hh:mma"));
	
	/**
	 * Default constructor for the NightlyPayCalculator.
	 */
	public NightlyPayCalculator() {
		super();
	}

	/**
	 * @param prebedRate
	 * @param bedToMidnightRate
	 * @param postMidnightRate
	 */
	public NightlyPayCalculator(double prebedRate, double bedToMidnightRate, double postMidnightRate) {
		super();
		this.preBedRate = prebedRate;
		this.bedToMidnightRate = bedToMidnightRate;
		this.postMidnightRate = postMidnightRate;
	}
	
	public double calculateGrandTotalDue(LocalDateTime start, LocalDateTime bedTime, LocalDateTime end) {
		double preBedTotal = 0f;
		double bedToMidnightTotal = 0f;
		double postMidnightTotal = 0f;
		LocalDateTime midnightDateTime = LocalDateTime.of(start.toLocalDate().plusDays(1), MIDNIGHT);
		
		preBedTotal = calculateSubTotalDue(start, bedTime, preBedRate);
		bedToMidnightTotal = calculateSubTotalDue(bedTime, midnightDateTime, bedToMidnightRate);
		postMidnightTotal = calculateSubTotalDue(midnightDateTime, end, postMidnightRate);
		
		return preBedTotal + bedToMidnightTotal + postMidnightTotal;
	}
	
	protected double calculateSubTotalDue(LocalDateTime start, LocalDateTime end, double rate) {
		long hours = start.until(end, ChronoUnit.HOURS);
		return rate * hours;
	}
	

	/**
	 * @return the prebedRate
	 */
	public double getPrebedRate() {
		return preBedRate;
	}

	/**
	 * @param prebedRate the prebedRate to set
	 */
	public void setPrebedRate(double prebedRate) {
		this.preBedRate = prebedRate;
	}

	/**
	 * @return the bedToMidnightRate
	 */
	public double getBedToMidnightRate() {
		return bedToMidnightRate;
	}

	/**
	 * @param bedToMidnightRate the bedToMidnightRate to set
	 */
	public void setBedToMidnightRate(double bedToMidnightRate) {
		this.bedToMidnightRate = bedToMidnightRate;
	}

	/**
	 * @return the postMidnightRate
	 */
	public double getPostMidnightRate() {
		return postMidnightRate;
	}

	/**
	 * @param postMidnightRate the postMidnightRate to set
	 */
	public void setPostMidnightRate(double postMidnightRate) {
		this.postMidnightRate = postMidnightRate;
	}
	
}
