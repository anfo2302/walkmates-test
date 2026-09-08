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
A few sentences: which tests/artefacts you produced and why those, against which requirements
(cite rule IDs, e.g. FR-1.3, FR-4.4).

### 2. What we found
The most interesting thing you learned or uncovered — a boundary bug, a surviving mutant, a
covered-but-buggy path, a fallback that didn't behave, a metamorphic relation that broke.

### 3. AI use (be honest — it doesn't lower your grade)
- What did you use AI for in this lab?
- **What did the AI suggest vs. what you kept or changed — and why?** (the key question)
- Anything the AI produced that you suspected was wrong or weak? How did you check?

We used AI in part A of the lab to interpret the questions, mainly 1.1.3 where we were unsure what was expected.  
We got different suggestions 

### 4. Judgment
Where did *you* have to decide something the tools/AI couldn't decide for you? (e.g. which
equivalence classes matter, whether coverage was "enough", whether a mutant was equivalent.)

### 5. What we'd test next
If you had another hour, what's the next test or risk you'd go after?

---

## Activity 2.1

### 1. What we did


### 2. What we found


### 3. AI use


### 4. Judgment


### 5. What we'd test next

---
<!-- Activity 2.2 and 2.3 draft -->

## 1. What we did

We produced the artefact [lab1-analysis.md](../lab1-analysis.md) together during a Teams call.  
All information was gathered from the module 1 study material, specifically
[Software Testing Course Notes M1: Quality Assurance and Fundamentals](https://sergiorico.github.io/software-testing-course-notes/M1-Quality-Assurance-Fundamentals/).
We chose to create this artefact ourselves.
We meant to ensure that the test coverage aligned precisely with the Lab instructions,
and to deepen our understanding of the module content by discussing the concepts collaboratively while writing.

## 2. What we found

## 3. AI use (be honest — it doesn't lower your grade)
- What did you use AI for in this lab?
- **What did the AI suggest vs. what you kept or changed — and why?** (the key question)
- Anything the AI produced that you suspected was wrong or weak? How did you check?

We used AI in part A of the lab to interpret the questions, mainly Activity 1.1 list-question 3,
because we were not sure what was meant by "quality *requirement*", thus unsure of what was expected.
When asking two different AI models, we got different suggestions.

In summary, Free Copilot said:
> A testable quality *requirement* must be:
>   - Measurable
>   - Observable
>   - Falsifiable (clear pass/fail condition)
>   - Tied to specific quality attribute (e.g., performance, security, reliability, maintainability)

After which it repeated the same explanation several times in a slightly different way.

We also experimented with using AI to generate the tests based on our
[lab1-analysis.md](../lab1-analysis.md) decision tables.
The AI produced structurally correct tests, but we did not trust them blindly.
We manually double-checked each suggestion against our decision tables,
and did not include any code we do not understand.

## 4. Judgment
Where did *you* have to decide something the tools/AI couldn't decide for you? (e.g. which
equivalence classes matter, whether coverage was "enough", whether a mutant was equivalent.)

We didn't trust AI with test coverage, as mentioned above, we created the [lab1-analysis.md](../lab1-analysis.md)
artefact ourselves because of this.

We verified whether it was considered best practise to put test constants at the top of the test class by googling,
people in a programming sub-reddit, and a Kotlin forum said; it is a bit of a "Religion" (preference based).
So, since they are constants meant for only this test class, we agreed with AI and decided that keeping them up top
for reusability and editability makes a lot of sense. Thus, it was kept as AI produced it.

## 5. What we'd test next
Question: If you had another hour, what's the next test or risk you'd go after?

Add addFunds test to confirm that balance only updates within two decimal places.
Add addFunds test which tests two decimal rounding behaviour.
Add test to confirm that addFunds reject negative values.
