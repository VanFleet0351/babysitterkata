package com.kylevanfleet.babysitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class NightlyPayCalculatorTest {

	//Error used for test dollar value returned
	public final double EPSILON = 0.01;
	//used for set up of date times in test cases
	//public final LocalDate CURRENT_DATE = LocalDate.now();
	//public final LocalDate TOMORROWS_DATE = LocalDate.now().plusDays(1);
	private final static String TODAY = "01/01/2021 ";
	private final static String TOMORROW = "01/02/2021 ";
	//public final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("hh:mma");
	public final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/uuuu hh:mma");
	
	
	//instance of class being tested
	private NightlyPayCalculator calc;
	

	@BeforeEach
	public void initEach() {
		calc = new NightlyPayCalculator();
	}
	

	@ParameterizedTest
	@MethodSource("grandTotalArguments")
	public void testCalculateGrandTotalDue(String startTime, String bedTime, String endTime, double expected, String message) {
		LocalDateTime[] times = getLocalDateTimes(startTime, bedTime, endTime);
		assertEquals(message, expected, calc.calculateGrandTotalDue(times[0], times[1], times[2]), EPSILON);
	}
	
	
	
	/**
	 * Converts String dates in the form of MM/dd/uuuu hh:mma, that is month/day/year time(AM/PM), to LocalDateTimes.
	 * The LocalDateTimes are returned as an Array in the same order as the arguments provided.
	 * 
	 * @param startTime
	 * @param bedTime
	 * @param endTime
	 * @return LocalDateTime[] where startTime is at index 0, bedTime is at index 1, and so on.
	 */
	private LocalDateTime[] getLocalDateTimes(String startTime, String bedTime, String endTime) {
		LocalDateTime[] times = {
				LocalDateTime.parse(startTime, DATETIME_FORMAT),
				LocalDateTime.parse(bedTime, DATETIME_FORMAT),
				LocalDateTime.parse(endTime, DATETIME_FORMAT)	
		};
		return times;
	}
	
	@Test
	void testCalculateSubTotalDue() {
		LocalDateTime startDateTime = LocalDateTime.parse(TODAY + "05:00PM", DATETIME_FORMAT);
		LocalDateTime bedDateTime = LocalDateTime.parse(TODAY + "10:00PM", DATETIME_FORMAT);
		
		assertEquals(60.00, calc.calculateSubTotalDue(startDateTime, bedDateTime, 12.00), EPSILON);
	}
	
	@Test
	void testCalculateTotalDueNewRate() {
		LocalDateTime[] times = getLocalDateTimes(TODAY + "05:00PM", TODAY + "10:00PM", TOMORROW + "04:00AM");
		NightlyPayCalculator newRateCalc = new NightlyPayCalculator(12.33, 8.50, 15.85);
		
		assertEquals(142.05, newRateCalc.calculateGrandTotalDue(times[0], times[1], times[2]), EPSILON);
	}

	private static Stream<Arguments> grandTotalArguments() {
		return Stream.of(
				Arguments.of(TODAY + "05:00PM", TODAY + "10:00PM", TOMORROW + "04:00AM", 140.00, "Should calculate Grand Total due with standard times"),
				Arguments.of(TODAY + "05:00PM", TODAY + "10:00PM", TODAY + "09:00PM", 48.00, "Should calculate Grand Total due with end time before bed time"),
				Arguments.of(TODAY + "05:00PM", TODAY + "10:00PM", TODAY + "11:00PM", 68.00, "Should calculate Grand Total due with end time before midnight"),
				Arguments.of(TODAY + "05:00PM", TOMORROW + "01:00AM", TOMORROW + "04:00AM", 148.00, "Should calculate Grand Total due with bed time after midnight"),
				Arguments.of(TODAY + "05:00PM", TOMORROW + "01:00AM", TOMORROW + "01:00AM", 100.00, "Should calculate Grand Total due when BedTime equals EndTime"),
				Arguments.of(TODAY + "05:00PM", TODAY + "11:00PM", TODAY + "11:00PM", 72.00, "Should calculate Grand Total due when BedTime equals EndTime before midnight"),
				Arguments.of(TODAY + "09:00PM", TODAY + "08:00PM", TOMORROW + "04:00AM", 88.00, "Should calculate Grand Total due when bed time is before start time"),
				Arguments.of(TODAY + "08:00PM", TODAY + "07:00PM", TOMORROW + "12:00AM", 32.00, "Should calculate Grand Total due when bed time is before start time"),
				Arguments.of(TOMORROW + "01:00AM", TODAY + "08:00PM", TOMORROW + "04:00AM", 48.00, "Should calculate Grand Total due when start time is after midnight"),
				Arguments.of(TODAY + "08:00PM", TODAY + "08:00PM", TOMORROW + "04:00AM", 96.00, "Should calculate Grand Total due when start time is at bed time")
				);

	}


//	
//	@Test
//	void testFractionalHoursRoundedDown() {
//		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
//		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
//		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
//		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
//		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
//		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
//		calc.setRounding(java.math.RoundingMode.DOWN);
//		
//		assertEquals(116.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
//	}
//	
//	@Test
//	void testFractionalHoursRoundedUp() {
//		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
//		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
//		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
//		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
//		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
//		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
//		calc.setRounding(java.math.RoundingMode.UP);
//		
//		assertEquals(152.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
//	}
//	
//	@Test
//	void testFractionalHoursRoundedHalfUP() {
//		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
//		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
//		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
//		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
//		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
//		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
//		calc.setRounding(java.math.RoundingMode.HALF_UP);
//		
//		assertEquals(140.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
//	}
//	
//	@Test
//	void testFractionalHoursRoundedHalfDown() {
//		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
//		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
//		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
//		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
//		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
//		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
//		calc.setRounding(java.math.RoundingMode.HALF_DOWN);
//		
//		assertEquals(132.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
//	}
//	
//	@Test
//	void testFractionalHoursRoundedHalfEven() {
//		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
//		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
//		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
//		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
//		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
//		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
//		calc.setRounding(java.math.RoundingMode.HALF_EVEN);
//		
//		assertEquals(140.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
//	}
//	
//	@Test
//	void testFractionalHoursRoundedCeiling() {
//		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
//		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
//		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
//		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
//		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
//		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
//		calc.setRounding(java.math.RoundingMode.CEILING);
//		
//		assertEquals(152.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
//	}
//	
//	@Test
//	void testFractionalHoursRoundedFloor() {
//		LocalTime startTime = LocalTime.parse("05:20PM", DATE_FORMAT);
//		LocalDateTime startDateTime = LocalDateTime.of(CURRENT_DATE, startTime);
//		LocalTime bedTime = LocalTime.parse("10:30PM", DATE_FORMAT);
//		LocalDateTime bedDateTime = LocalDateTime.of(CURRENT_DATE, bedTime);
//		LocalTime endTime = LocalTime.parse("03:45AM", DATE_FORMAT);
//		LocalDateTime endDateTime = LocalDateTime.of(TOMORROWS_DATE, endTime);
//		calc.setRounding(java.math.RoundingMode.FLOOR);
//		
//		assertEquals(116.00, calc.calculateGrandTotalDue(startDateTime, bedDateTime, endDateTime), EPSILON);
//	}
//	
	@Test
	void testSetRoundingThrowsException() {
		//check for proper exception thrown
		assertThrows(IllegalArgumentException.class,
				() -> {calc.setRounding(java.math.RoundingMode.UNNECESSARY);});
	}
}