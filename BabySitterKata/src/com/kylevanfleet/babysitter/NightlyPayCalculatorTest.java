package com.kylevanfleet.babysitter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

class NightlyPayCalculatorTest {
	
	//Error used for test dollar value returned
	public final double EPSILON = 0.01;
	public final LocalDate CURRENT_DATE = LocalDate.now();
	public final LocalDate TOMORROWS_DATE = LocalDate.now().plusDays(1);
	public final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("hh:mma");
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
	void testCalculatePreBedTotalDue() {
		LocalTime startTime = LocalTime.parse("05:00PM", DATE_FORMAT);
		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
		LocalTime bedTime = LocalTime.parse("10:00PM", DATE_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
		
		assertEquals(60.00, calc.calculateSubTotalDue(startDateTime, bedDateTime, 12.00), EPSILON);
	}
	

}