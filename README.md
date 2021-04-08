# babysitterkata
A small java project attempting to solve the following problem.

Problem:
----------

Babysitter Kata

Background

This kata simulates a babysitter working and getting paid for one night.  The rules are pretty straight forward:

The babysitter 
- starts no earlier than 5:00PM
- leaves no later than 4:00AM
- gets paid $12/hour from start-time to bedtime
- gets paid $8/hour from bedtime to midnight
- gets paid $16/hour from midnight to end of job
- gets paid for full hours (no fractional hours)


Feature:
As a babysitter
In order to get paid for 1 night of work
I want to calculate my nightly charge


Solution:
---------
The babysitter may calculate his/her nightly charge by calling the calculateGrandTotalDue method from the 
NightlyPayCalculator. The babysitter has to the ability to set their hourly rate during construction of the
NightlyPayCalculator or individually via mutator methods. Since the babysitter gets paid for full hours only
he/she has the ability to change how the minutes are rounded via the setRounding method. NightlyPayCalculator
supports all forms of rounding from java.math.RoundingMode except the UNNECESSARY RoundingMode. The default 
rounding is set to DOWN.