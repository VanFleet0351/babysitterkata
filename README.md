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

Notes on solution
-----------------
Possible classes:
	Baby sitter, pay calculator

Baby Sitter: Uses calculator to find hours between start, bed, and end time. Uses calculator to find total 
dollar amount owed based on hours worked.

Pay Calculator: calculate number of hours from start to bed time given those times. Calculate number of hours
from bed time to midnight given bed time. Calculate number of hours from midnight to end time given end time.
Calculate total due for pre-bedtime work. Calculate total due for bed time to midnight work. Calculate total due
for post-midnight work. Calculate grand total due for whole nights work.

preconditions:
1. Start time at or later than 5:00 pm.
2. End time is at or before 4:00 am.

instance variables:
1. Start-bed time rate: $12/hr
2. Bed-midnight rate: $8/hr
3. Midnight-end time rate: $16/hr
*These could change in the future

be sure to test for:
1. Start time is after bed time.
2. Start time is after midnight.
3. Bed time is after midnight.
4. fractional hours