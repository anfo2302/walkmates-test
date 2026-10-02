# Lab Reflection — WalkMates

**Lab:** 2  
**Pair:** Anna Forslund & Andreas Wallgren  
**Repo commit/tag:** [Lab 2 branch](https://github.com/anfo2302/walkmates-test/tree/lab2)  

---

### 1. What we did
A few sentences: which tests/artifacts you produced and why those, against which requirements
(cite rule IDs, e.g. FR-1.3, FR-4.4).

### 2. What we found
The most interesting thing you learned or uncovered — a boundary bug, a surviving mutant, a
covered-but-buggy path, a fallback that didn't behave, a metamorphic relation that broke.

### 3. AI use (be honest — it doesn't lower your grade)
- What did you use AI for in this lab?
- **What did the AI suggest vs. what you kept or changed — and why?** (the key question)
- Anything the AI produced that you suspected was wrong or weak? How did you check?

I considered removing `throws PaymentService.PaymentException` from
`SeekerService.declinedPaymentDoesNotCreditWallet` (before changing my mind after reading up on it)
because the test method catches it via AssertJ's `assertThatThrownBy`, so it never actually escapes.
I verified this by checking the [JDOM Exception documentation](https://www.jdom.org/pipermail/jdom-interest/2002-April/009557.html)
and a free Java E-book [Effective Java - Chapter 10 - Exceptions](https://github.com/GunterMueller/Books-3/blob/master/Effective%20Java%20(3rd%20Edition).pdf)
on GitHub.

Removed `@Mock NotificationService` in `SeekerServiceTest`.
`SeekerService` injects a `NotificationService` and stores it in a `private final field`,
but never calls it. Similarly, the AI-generated `SeekerServiceTest` mirrored this:
it declared `@Mock NotificationService` but never stubs or verifies it,
so the test gives the impression the dependency is exercised when it isn't.
This is a defect either way. If notifications were meant to be sent from `SeekerService`,
a behaviour is missing and untested.
If they weren't, the constructor is advertising a responsibility the class doesn't fulfil.

### 4. Judgment
Where did *you* have to decide something the tools/AI couldn't decide for you? (e.g. which
equivalence classes matter, whether coverage was "enough", whether a mutant was equivalent.)

### 5. What we'd test next
If you had another hour, what's the next test or risk you'd go after?

Expand the booking service tests (failure paths: (BookingRejectedException), duration out of range)
Expand the seeker service tests (failure paths: (IllegalArgumentException))


---
### [Lab 2 Regression Selection Analysis](../lab2-regression-selection.md): [GitHub Link](https://github.com/anfo2302/walkmates-test/blob/lab2/lab2-regression-selection.md)
