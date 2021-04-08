package com.kylevanfleet.babysitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;


/**
 * The babysitter may calculate his/her nightly charge by calling the calculateGrandTotalDue method from the 
 * NightlyPayCalculator. The babysitter has to the ability to set their hourly rate during construction of the
 * NightlyPayCalculator or individually via mutator methods. Since the babysitter gets paid for full hours only
 * he/she has the ability to change how the minutes are rounded via the setRounding method. NightlyPayCalculator
 * supports all forms of rounding from java.math.RoundingMode except the UNNECESSARY RoundingMode.
 * 
 * @author Kyle Van Fleet
 */
public class NightlyPayCalculator {
	
	//Hourly babysitting rates
	private double preBedRate = 12.00;
	private double bedToMidnightRate = 8.00;
	private double postMidnightRate = 16.00;
	
	//The manner in which fraction hours are handled
	private RoundingMode rounding = RoundingMode.DOWN;
	

	//Non-dated midnight
	private final static LocalTime MIDNIGHT_TIME = LocalTime.parse("12:00AM", DateTimeFormatter.ofPattern("hh:mma"));
	
	/**
	 * Default constructor for the NightlyPayCalculator.
	 */
	public NightlyPayCalculator() {
		super();
	}

	/**
	 * @param prebedRate The hourly rate charged before bedtime.
	 * @param bedToMidnightRate The hourly rate charged after bedtime but before midnight.
	 * @param postMidnightRate The hourly rate charged after midnight.
	 */
	public NightlyPayCalculator(double prebedRate, double bedToMidnightRate, double postMidnightRate) {
		super();
		this.preBedRate = prebedRate;
		this.bedToMidnightRate = bedToMidnightRate;
		this.postMidnightRate = postMidnightRate;
	}
	
	
	/**
	 * Calculated the grand total charge based on the times provided.
	 * 
	 * @param start The time and date of when the babysitting session started.
	 * @param bedTime The time that the child goes to bed.
	 * @param end The time the babysitting session ends.
	 * @requires start is before end and start is at or after 5:00pm and end is at or before 4:00am.
	 * 
	 * @return The dollar amount the the babysitter is due at the end of the session based on the
	 *  the stored hourly rates.
	 */
	public double calculateGrandTotalDue(LocalDateTime start, LocalDateTime bedTime, LocalDateTime end) {
		LocalDateTime midnight = getMidnightDateTime(start);
		LocalDateTime[] times = {start, bedTime, midnight, end};
		Arrays.sort(times);
		
		int i = 0;
		LocalDateTime currentTime = start, nextTime = times[i];
		double currentRate = preBedRate, total = 0f;
		
		while(currentTime.isBefore(end) && i < 4) {
			nextTime = times[++i];
			if(currentTime.isAfter(bedTime) || currentTime.isEqual(bedTime)) currentRate = bedToMidnightRate;
			if(currentTime.isAfter(midnight) || currentTime.isEqual(midnight)) currentRate = postMidnightRate;
			
			total += calculateSubTotalDue(currentTime, nextTime, currentRate);
			
			if(currentTime.isBefore(nextTime)) currentTime = nextTime;
		}
		
		return total;
	}

	/**
	 * Provides the correct midnight date and time based on the context provided by the start time.
	 * 
	 * @param start The start time of the babysitting session.
	 * @return The localDateTime of midnight for the babysitting session.
	 */
	private LocalDateTime getMidnightDateTime(LocalDateTime start) {
		LocalDate midnightDate = start.toLocalDate();
		//if start is a pm value midnight will be on the next day
		if(start.getHour() > 12) midnightDate = midnightDate.plusDays(1);
		
		return LocalDateTime.of(midnightDate, MIDNIGHT_TIME);
	}
	
	/**
	 * Calculate the payment due after completion of a babysitting sub-session.
	 * 
	 * @param start The start time and date of the babysitting sub-session.
	 * @param end	The end time and date of the babysitting sub-session.
	 * @param rate The hourly rate for this babysitting sub-session.
	 * @return The dollar amount due after this babysitting sub-session.
	 */
	protected double calculateSubTotalDue(LocalDateTime start, LocalDateTime end, double rate) {
		long hours = start.until(end, ChronoUnit.HOURS);
		
		LocalDateTime tempStart = start.plusHours(hours);
		long minutes = tempStart.until(end, ChronoUnit.MINUTES);
		BigDecimal fractionalHours = new BigDecimal(hours + (minutes/ 60.0));
		
		hours = fractionalHours.setScale(0, rounding).longValue(); 
		
		if(hours < 0) hours = 0;
		
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
	
	/**
	 * @return the rounding
	 */
	public RoundingMode getRounding() {
		return rounding;
	}

	/**
	 * Sets the type of rounding used for fractional hours. UNNECESSARY roundingMode is not
	 * supported.
	 * 
	 * @param rounding A java.math.RoundingMode that determines how minutes are rounded to hours.
	 * @throws IllegalArgumentException Thrown when UNNECESSARY is passed as the argument.
	 * @requires rounding not equal to RoundingMode.UNNECESSARY
	 */
	public void setRounding(RoundingMode rounding) throws IllegalArgumentException {
		if(rounding.equals(RoundingMode.UNNECESSARY)) throw new IllegalArgumentException("UNNECESSARY RoundingMode is not supported");
		this.rounding = rounding;
	}
}
