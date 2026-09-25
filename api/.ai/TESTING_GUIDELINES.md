# 📘 Testing Guidelines -- Java Projects

## 🎯 Purpose

Establish a clear and consistent naming convention for tests in Java
projects (JUnit 5 + Spring), improving readability, maintainability, and
scalability.

------------------------------------------------------------------------

# 1. Official Naming Convention

## ✅ Standard Format (Recommended)

should\[ExpectedBehavior\]When\[Condition\]

### Examples

shouldReturnAgendaWhenIdExists\
shouldThrowExceptionWhenAgendaNotFound\
shouldUpdateStatusWhenSessionIsOpen\
shouldRetryWhenOptimisticLockExceptionOccurs

### Why this format?

- Behavior-driven
- Clear in test reports
- Scales well in large codebases
- Encourages business-focused thinking

------------------------------------------------------------------------

# 2. Alternative Pattern (For Complex Rules)

## Given / When / Then

given\[Context\]When\[Action\]Then\[ExpectedResult\]

### Examples

givenExistingAgendaWhenFindByIdThenReturnAgenda\
givenClosedSessionWhenVoteThenThrowException

⚠ Use only when business rules are complex.\
Avoid excessively long method names.

------------------------------------------------------------------------

# 3. Naming by Test Type

------------------------------------------------------------------------

## 🧪 3.1 Unit Tests (Use Cases / Services)

Focus: Business behavior.

### Good Examples

shouldCreateAgendaWithValidData\
shouldNotCreateAgendaWhenTitleIsNull\
shouldCloseSessionWhenTimeExpires\
shouldCalculateTotalAmountCorrectly

### ❌ Avoid

testCreate\
createAgendaTest\
agendaTest1

These do not describe behavior.

------------------------------------------------------------------------

## 🧪 3.2 Exception Tests

Always describe:

- What fails
- Why it fails

### Examples

shouldThrowBusinessExceptionWhenSessionIsClosed\
shouldThrowResourceNotFoundWhenAgendaDoesNotExist\
shouldThrowOptimisticLockExceptionWhenVersionIsOutdated

Avoid generic names like:

shouldThrowException

------------------------------------------------------------------------

## 🧪 3.3 Integration Tests

### Repository

shouldPersistAgendaInDatabase\
shouldLoadCartWithItemsUsingJoinFetch

### Controller

shouldReturn201WhenAgendaIsCreated\
shouldReturn404WhenAgendaNotFound\
shouldReturn400WhenPayloadIsInvalid

------------------------------------------------------------------------

## 🧪 3.4 Optimistic Locking Tests

shouldFailWhenUpdatingWithOutdatedVersion\
shouldRetryOperationWhenOptimisticLockingFails

If using @Retryable:

shouldRetrySaveWhenOptimisticLockExceptionOccurs

------------------------------------------------------------------------

# 4. Test Class Naming

## Unit Tests

ClassNameTest

Examples:

CreateAgendaHandlerTest\
AgendaRepositoryTest\
VotingSessionServiceTest

## Integration Tests

ClassNameIT\
or\
ClassNameIntegrationTest

Examples:

AgendaRepositoryIT\
AgendaControllerIntegrationTest

------------------------------------------------------------------------

# 5. Test Structure (AAA Pattern)

Arrange -- Prepare data\
Act -- Execute behavior\
Assert -- Validate result

Example:

@Test void shouldCloseSessionWhenTimeExpires() { // Arrange var session
= Session.openWithExpiration(...);

    // Act
    session.closeIfExpired();

    // Assert
    assertThat(session.getStatus()).isEqualTo(CLOSED);

}

------------------------------------------------------------------------

# 6. Use @Nested for Scenario Organization

@Nested class WhenSessionIsClosed {

    @Test
    void shouldNotAllowVote() {}

    @Test
    void shouldThrowException() {}

}

Benefits:

- Better readability
- Organized reports
- Clear scenario grouping

------------------------------------------------------------------------

# 7. Test Behavior, Not Implementation

❌ Wrong:

shouldCallRepositorySave

✅ Correct:

shouldPersistAgendaAfterCreation

Tests must validate observable behavior, not internal implementation
details.

------------------------------------------------------------------------

# 8. Golden Rules

✔ Always start with "should"\
✔ Describe expected result before condition\
✔ Avoid generic words like "test"\
✔ Do not number tests\
✔ The method name must explain the scenario alone\
✔ Prefer business language over technical jargon

------------------------------------------------------------------------

# 9. Official Project Standard

For Java + Spring + JPA projects, the official standard is:

should\[ExpectedBehavior\]When\[Condition\]

This is clean, professional, and scalable for enterprise systems.

------------------------------------------------------------------------

# ✅ Final Checklist

Before committing a test, verify:

- Does the name clearly describe behavior?
- Can someone understand the scenario without opening the test?
- Is the naming consistent with the rest of the project?
- Is the focus on business behavior rather than implementation?

------------------------------------------------------------------------

End of document.
