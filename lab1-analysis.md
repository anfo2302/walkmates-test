# Lab 1 Analysis

## Part A

### Activity 1.1

#### 1. 
FR-1.1 Registration

FR-1.3 Wallet

FR-4.4 Booking creation
#### 2.

FR-1.1 Registration - Functional suitability, interaction capability

Registration must be able to validate all fields correctly and communicate the conditions and requirements for membership to the user.   

FR-1.3 Wallet - Safety, security

Whenever a user's money is involved it is vital that it is kept safe from unauthorized outside actors as well as from 
internal mistakes.

FR-4.4 Booking creation - Reliability, interaction capability

It is important that the booking system performs reliably and clearly communicates what is needed for a successful booking
request or why a request has been denied.

#### 3.

| Testing concept | FR-1.1                                                                                                      |
|-----------------|-------------------------------------------------------------------------------------------------------------|
| Test basis      | Requirement FR-1.1 Display name MUST be 2–40 characters inclusive, letters/spaces/hyphens/apostrophes only. |  
| Test Condition  | Upper boundary behavior.                                                                                    |
| Test case | Try to register a Seeker with a display name that is 40 characters long. |
| Oracle | The requirement says 40 is valid |
| Expected outcome | The registration validation accepts the display name |
| Related boundary cases | 1, 2, 3, 39, 41 |

### Activity 1.2

#### 1. 

The likely human error is that the developer forgot to make the maximum booking condition equal to or greater than.
The fault lies in `BookingService::createBooking(..)` where the condition that checks maximum bookings is missing an `=`
to check if the limit has been reached, right now it only checks if it has been exceeded, allowing a seeker one extra 
booking per TrustTier. The resulting failure is that the user is able to make more bookings than their trust tier allows.

#### 2.

The fault in the code is found in `BookingService::createBooking(..)`:

```
        long seekerActive = activeBookingCountForSeeker(seekerId);
        if (seekerActive > seeker.getMaxConcurrentBookings()) {
            throw new BookingRejectedException("Seeker booking limit reached for tier " + seeker.getTrustTier());
        }
```
The current number of bookings for the user is counted and compared to the allowed maximum number of bookings for their
trust tier. The faulty condition only checks if the current number of bookings exceeds the maximum, not if the maximum has been reached.   

#### 3.

The fault should have been caught at the unit testing level. Using boundary value analysis and testing edge cases would 
have caught the fault during unit testing.

## Part B

### Activity 2.1

Email:

| Class                     | Input            | Expected outcome |
|---------------------------|------------------|------------------|
| Invalid format @          | user.example.com | Rejected         | 
| Invalid format .          | user@examplecom  | Rejected         | 
| Invalid format local part | @example.com     | Rejected         | 
| Valid format              | user@example.com | Accepted         |
| Valid length              | user@example.com | Accepted         | 
| Invalid length            | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa<br/>aaaaaaaaa@bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb<br/>bbbbbbbb.cccccccccccccccccccccccccccccccccccccccccccc<br/>ccccccccccccccccccc.dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd      | Rejected         | 

Display name:

| Class                | Input                                        | Expected outcome |
|----------------------|----------------------------------------------|------------------|
| Invalid length short | u                                            | Rejected         | 
| Invalid length long  | useraaaaaaaaaaaaaaaaaaa<br/>aaaaaaaaaaaaaaaaaaaaa | Rejected         | 
| Valid length         | user                                         | Accepted         | 
| Invalid characters   | 123#@                                        | Rejected         | 
| Valid characters     | user -'                                      | Accepted         |

Phone number:

| Class                              | Input         | Expected outcome |
|------------------------------------|---------------|------------------|
| Invalid format Swedish             | 0501234567    | Rejected         | 
| Invalid format international       | +1231234567   | Rejected         | 
| Valid format Swedish               | 0701234567    | Accepted         | 
| Valid format international         | +46701234567  | Accepted         | 
| Invalid length short Swedish       | 070123456     | Rejected         | 
| Invalid length long Swedish        | 07012345678   | Rejected         | 
| Invalid length short international | +4670123456   | Rejected         | 
| Invalid length long international  | +467012345678 | Rejected         | 

Wallet top-up amount:

| Class                      | Input                   | Expected outcome |
|----------------------------|-------------------------|------------------|
| Invalid top-up amount low  | 9.0                     | Rejected         | 
| Invalid top-up amount high | 5001.0                  | Rejected         | 
| Valid top-up amount        | 10.0                    | Accepted         | 
| Invalid resulting balance  | 5000.0 repeated 5 times | Rejected         | 
| Valid resulting balance    | 10.0                    | Accepted         | 



### Activity 2.2 

| Purpose                          | Value    | Expected result |
|----------------------------------|----------|-----------------|
| Just below lower top-up boundary | 9.99     | Rejected        | 
| Lower top-up boundary            | 10.00    | Accepted        | 
| Just above lower top-up boundary | 10.01    | Accepted        | 
| Just below upper top-up boundary | 4999.99  | Accepted        | 
| Upper top-up boundary            | 5000.00  | Accepted        | 
| Just above upper top-up boundary | 5000.01  | Rejected        | 
| Just below maximum balance       | 19999.99 | Accepted        | 
| Maximum balance                  | 20000.00 | Accepted        | 
| Just above maximum balance       | 20000.01 | Rejected        | 

### Activity 2.3

| Trust tier | Max concurrent bookings | Platform fee |
|------------|-------------------------|--------------|
| New        | 1                       | 15%          | 
| Verified   | 3                       | 12%          | 
| Trusted    | 5                       | 8%           | 
| Pro-sitter | 10                      | 5%           | 


| Condition/action        | Rule 1 | Rule 2   | Rule 3  | Rule 4     |
|-------------------------|--------|----------|---------|------------|
| Trust tier              | New    | Verified | Trusted | Pro-sitter |
| Max concurrent bookings | 1      | 3        | 5       | 10         | 
| Platform fee            | 15%    | 12%      | 8%      | 5%         |
