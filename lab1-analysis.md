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

| Class | Input | Expected outcome |
|-------|-------|------------------|
|       |       |                  | 

### Activity 2.2 

| Purpose | Value | Expected result |
|---------|-------|-----------------|
|         |       |                 | 

### Activity 2.3

| Trust tier | Max concurrent bookings | Platform fee |
|------------|-------------------------|--------------|
| New        |                         |              | 
| Verified   |       |                 | 
|            |       |                 | 
|            |       |                 | 
|            |       |                 | 