package com.kylevanfleet.babysitter;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

class NightlyPayCalculatorTest {
	
	//Error used for test dollar value returned
	public final double EPSILON = 0.01;
	//used for set up of date times in test cases
	public final LocalDate CURRENT_DATE = LocalDate.now();
	public final LocalDate TOMORROWS_DATE = LocalDate.now().plusDays(1);
	public final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("hh:mma");
	//instance of class being tested
	public NightlyPayCalculator calc = new NightlyPayCalculator();

	@Test
	void testCalculateGrandTotalDue() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("04:00AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		
		assertEquals(140.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testCalculateSubTotalDue() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		
		assertEquals(60.00, calc.calculateSubTotalDue(startDateTime, bedDateTime, 12.00), EPSILON);
	}
	
	@Test
	void testCalculateTotalDueNewRate() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("04:00AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		NightlyPayCalculator newRateCalc = new NightlyPayCalculator(12.33, 8.50, 15.85);
		assertEquals(142.05, newRateCalc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	
	@Test
	void testEndTimeBeforeBedTime() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("09:00PM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(CURRENT_DATE, endTime);
		
		assertEquals(12.00 * 4, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	
	@Test
	void testEndTimeBeforeMidnight() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("11:00PM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(CURRENT_DATE, endTime);
		
		assertEquals(12.00 * 5 + 8.00 * 1, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}

	@Test
	void testBedTimeAfterMidnight() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("01:00AM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(TOMORROWS_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("04:00AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		
		assertEquals(12.00 * 7 + 16.00 * 4, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testBedTimeEqualsEndTime() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("01:00AM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(TOMORROWS_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("01:00AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		
		assertEquals(12.00 * 7 + 16.00 * 1, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testBedTimeEqualsEndTimeBeforeMidnight() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("11:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("11:00PM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(CURRENT_DATE, endTime);
		
		assertEquals(12.00 * 6, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testStartTimeAfterBedTime() {
		LocalTime startTime = LocalTime.parse("09:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("08:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("04:00AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		
		assertEquals(8.00 * 3 + 16.00 * 4, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testStartTimeAfterMidnight() {
		LocalTime startTime = LocalTime.parse("01:00AM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(TOMORROWS_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("08:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("04:00AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		
		assertEquals(16.00 * 3, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testStartTimeAtBedTime() {
		LocalTime startTime = LocalTime.parse("08:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("08:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("04:00AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		
		assertEquals(8.00 * 4 + 16.00 * 4, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testFractionalHoursRoundedDown() {
		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		calc.setRounding(java.math.RoundingMode.DOWN);
		
		assertEquals(116.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testFractionalHoursRoundedUp() {
		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		calc.setRounding(java.math.RoundingMode.UP);
		
		assertEquals(152.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testFractionalHoursRoundedHalfUP() {
		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		calc.setRounding(java.math.RoundingMode.HALF_UP);
		
		assertEquals(140.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testFractionalHoursRoundedHalfDown() {
		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		calc.setRounding(java.math.RoundingMode.HALF_DOWN);
		
		assertEquals(132.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testFractionalHoursRoundedHalfEven() {
		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		calc.setRounding(java.math.RoundingMode.HALF_EVEN);
		
		assertEquals(140.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testFractionalHoursRoundedCeiling() {
		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		calc.setRounding(java.math.RoundingMode.CEILING);
		
		assertEquals(152.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testFractionalHoursRoundedFloor() {
		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
		calc.setRounding(java.math.RoundingMode.FLOOR);
		
		assertEquals(116.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
	}
	
	@Test
	void testSetRoundingThrowsException() {
		//check for proper exception thrown
		assertThrows(IllegalArgumentException.class,
				() -> {calc.setRounding(java.math.RoundingMode.UNNECESSARY);});
	}
}