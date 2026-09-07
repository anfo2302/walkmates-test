# Lab Reflection — WalkMates

**Lab:** 1  
**Pair:** Anna Forslund & Andreas Wallgren  
**Repo commit/tag:** (link — Labs 1–3; write `N/A` for Lab 4)  
---

---

## Reflection Template

> Keep it **short and specific** — this is graded for *understanding*, not length.
> Half a page to a page is plenty. Bullet points are fine.

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

We used AI in part A of the lab to interpret the questions, mainly 1.1.3 where we were unsure what was expected. We got different suggestions 

### 4. Judgment
Where did *you* have to decide something the tools/AI couldn't decide for you? (e.g. which
equivalence classes matter, whether coverage was "enough", whether a mutant was equivalent.)

### 5. What we'd test next
If you had another hour, what's the next test or risk you'd go after?

---

## Activity 2.1

### 1. What we did

We filled out the equivalence partitioning tables for FR-1.1 (email, phone number, and display name) and used ChatGPT to create 
unit tests based on the tables. It returned two tests per table, one to assert that valid values are accepted and 
one to ensures that invalid values are rejected. Parameterized test were used where the same behavior was tested with different input. We then evaluated the AI made tests to make sure that they covered everything and behaved as expected. 

### 2. What we found

We found an error in the regex pattern for international phone numbers that we had to correct to fulfill the 
requirement. 

### 3. AI use

We used AI to produce unit tests based on our tables and evaluated what it produced. For 2.1 it created tests that covered all the necessary requirements, but it tested all the invalid classes in the same tests. 

The AI did not produce anything that we suspected was wrong and since it only made simple unit tests it was fairly easy to check everything manually. The AI created a lot of parameterized tests which could make diagnosing more difficult if they are used improperly. However, when we looked at the situations in which parameterized testing was implemented we saw that it was always used to test a single behavior but with different input so we kept it.

### 4. Judgment

We created all the tables manually with help of the course notes so we mainly had to determine whether the AI produced test cases were correct and had sufficient coverage. 

### 5. What we'd test next

The next test

---
## Activity 2.2

### 1. What we did

### 2. What we found

### 3. AI use

### 4. Judgment

### 5. What we'd test next

---
## Activity 2.3

### 1. What we did


### 2. What we found


### 3. AI use


### 4. Judgment


### 5. What we'd test next
