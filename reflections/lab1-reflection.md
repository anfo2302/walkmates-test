# Lab Reflection — WalkMates

**Lab:** 1  
**Pair:** Anna Forslund & Andreas Wallgren  
**Repo commit/tag:** [Lab 1 branch](https://github.com/anfo2302/walkmates-test/tree/lab1)
---

## 1. What we did

We produced the artefact [lab1-analysis.md](https://github.com/anfo2302/walkmates-test/blob/lab1/reflections/lab1-reflection.md) together during a Teams call.  
All information was gathered from the module 1 study material, specifically
[Software Testing Course Notes M1: Quality Assurance and Fundamentals](https://sergiorico.github.io/software-testing-course-notes/M1-Quality-Assurance-Fundamentals/).
We chose to create this artefact ourselves.
We meant to ensure that the test coverage aligned precisely with the Lab instructions,
and to deepen our understanding of the module content by discussing the concepts collaboratively while writing.

We then used ChatGPT to create unit tests based on the tables.
It returned two tests per table, one to assert that valid values are accepted and
one to ensures that invalid values are rejected.
Parameterized test were used where the same behaviour was tested with different input.
We then evaluated the AI made tests to make sure that they covered everything and behaved as expected.

## 2. What we found

We found an error in the regex pattern for international phone numbers that we had to correct to fulfill the
requirement.

In a real project, we would rely on a mature and RFC-compliant email-validation library rather than
re-implementing the rules ourselves. However, since this lab focuses on learning how to design and write tests,
we chose to follow the simplified FR-1.1 requirements.

## 3. AI use (be honest — it doesn't lower your grade)

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
[lab1-analysis.md](https://github.com/anfo2302/walkmates-test/blob/lab1/reflections/lab1-reflection.md) decision tables.
The AI produced structurally correct tests, but we did not trust them blindly.
We manually double-checked each suggestion against our decision tables,
and did not include any code we did not understand.

The AI created a lot of parameterised tests which could make diagnosing more difficult if they are used improperly.
However, when we looked at the situations in which parameterised testing was implemented we saw that it was always
used to test a single behaviour but with different input so we kept it.

## 4. Judgment

We didn't trust AI with test coverage, as mentioned above, we created the [lab1-analysis.md](https://github.com/anfo2302/walkmates-test/blob/lab1/reflections/lab1-reflection.md)
artefact ourselves because of this.

We verified whether it was considered best practise to put test constants at the top of the test class by googling,
people in a programming sub-reddit, and a Kotlin forum said; it is a bit of a "Religion" (preference based).
So, since they are constants meant for only this test class,
we agreed with AI and decided that keeping them up top
for reusability and editability makes a lot of sense. Thus, it was kept as AI produced it.

The AI generated tests where all invalid options were evaluated in a single test for each subtask,
meaning that testing display names with invalid length and format was performed in the same test.
We discussed whether to separate those tests further to match the tables more closely or to keep the structure
as the test logic and behaviour were very similar. We decided to keep the tests unchanged as the TODO in the
test file also stated that there should be one valid and one invalid equivalence class for email, name and, phone.

## 5. What we'd test next

Add test that confirms a new `Seeker` starts with `NEW` as initial trust tier.

Add `addFunds` test to confirm that balance only updates within two decimal places.
Add `addFunds` test which tests two decimal rounding behaviour.
Add test to confirm that `addFunds` reject negative values.

Add tests for `charge` behaviour similar to the FR-1.3 rules, since this is another public seeker method
that can mutate the seeker's balance.
