# Design Decisions — Task Management System

## Context
Source artifacts reviewed: UML class diagram (`uml_mermaid_fixed.mmd`), CLI design (`ConsoleMenu`), and validation utilities (`ValidationUtils`). Project language: `java`. Goal: produce concise rationale and decisions that guided the current implementation and explain trade\-offs and future directions.

## Goals
1. Clear separation of concerns: presentation (CLI), business logic (services), domain models (entities), and utilities (validation, reporting).
2. Robust CLI input handling with centralized validation and user-friendly prompts/hints.
3. Testable services by keeping business rules out of the CLI.
4. Simple, extensible reporting via DTOs.

## High-level Architecture Decision
- Pattern: Layered architecture
    1. Models — domain objects (`User`, `Project`, `Task`, `ProjectStatusReportDto`)
    2. Services — business logic and state (`UserService`, `ProjectService`, `TaskService`, `ReportService`)
    3. Presentation — `ConsoleMenu` (thin CLI wrapper that delegates to services)
    4. Utilities — `ValidationUtils` for input rules
- Rationale: Separation enables unit testing services independently from CLI and simplifies replacing the UI later.

## Models and Inheritance
- `User` is abstract with concrete `RegularUser` and `AdminUser`.
- `Project` is abstract with `SoftwareProject` and `HardwareProject`.
- `Task` references `projectId` and `assignedUserId`.
- Decision rationale: Use inheritance for behavioral specialization (e.g., `displayRole`, project-specific attributes) while using identifiers (IDs) to link entities avoids deep object graphs and simplifies serialization/storage.

## Service Layer Responsibilities
- `UserService`: user creation, login state, user lookup.
- `ProjectService`: project lifecycle, assignment operations, reporting helpers.
- `TaskService`: task lifecycle, completion metrics.
- `ReportService`: aggregates using `ProjectService` and `TaskService` into `ProjectStatusReportDto`.
- Decision: Services own in-memory repositories (lists). Rationale: small CLI app; keeps persistence concerns isolated for future replacement.

## Validation Strategy
- Centralize rules in `ValidationUtils`:
    - Allowed task statuses (e.g., TODO, IN_PROGRESS, DONE)
    - Allowed project types
    - Email regex
    - Positive team size, non-negative budget, budget range checks
    - Non-empty text and basic date format validation
- CLI helpers in `ConsoleMenu`: `readNonEmptyText`, `readPositiveInt`, `readNonNegativeDouble`, `readValidTaskStatus`, `readExistingProjectId`, etc.
- Decision rationale: Single source of truth reduces duplication; CLI handles re-prompting and user feedback.

## Input Flow & UX Guidelines
- Pattern: prompt → validate via `ValidationUtils` → if invalid, print clear error + hint → retry.
- Prompts include accepted formats and allowed values (e.g., "Enter task status \`(TODO, IN_PROGRESS, DONE)\`:").
- Existence checks rely on services (e.g., `ProjectService.getProjectById`).

## Permissions and Session
- `UserService` maintains `currentUser`.
- `ConsoleMenu` implements guard methods: `isAdmin()`, `isLoggedIn()`, `checkAdminPermission(action)`, `checkLoggedInPermission(action)`.
- Decision: Lightweight session model inside `UserService` keeps CLI state minimal and centralized.

## Error Handling
- CLI does not throw; it catches parsing issues (NumberFormatException) and reports friendly messages.
- Services return booleans or optional results for operation success; CLI prints explanations.
- Rationale: CLI-first UX; keep exceptions for unexpected internal errors.

## DTOs and Reporting
- `ProjectStatusReportDto` used to return summarized, read\-only reporting data.
- `ReportService` composes DTOs by querying `ProjectService` and `TaskService`.
- Decision: DTOs decouple report consumers from domain shapes and allow easy extension.

## Trade-offs and Consequences
- In-memory lists inside services:
    - + Fast to implement, easy to test.
    - - Not durable; requires migration to persistence later.
- ID-based associations:
    - + Simpler references and easier serialization.
    - - Slightly more indirection compared to direct object references.
- CLI coupling:
    - + Quick user feedback and iterative development.
    - - Business logic must remain strictly in services to avoid brittle UI tests.

## Alternatives Considered
1. Full ORM / persistent store now vs in-memory:
    - Chosen: in-memory for simplicity. Persist later behind repository interfaces.
2. Object references between models vs ID references:
    - Chosen: ID references to keep models lightweight and avoid circular dependencies in a CLI context.
3. Throwing exceptions for validation vs boolean / error messages:
    - Chosen: non-exception control flow for expected user errors; exceptions reserved for programmer errors.

## Testing Strategy
- Unit tests for services and `ValidationUtils` (mocking not required for in-memory lists).
- Integration tests covering workflows via services (project creation → task assignment → report generation).
- CLI tests: minimal, focusing on top-level flows; main verification should be on services.

## Extensibility & Migration Notes
- Introduce repository interfaces (e.g., `ProjectRepository`) to swap in persistence without changing services.
- Replace `ConsoleMenu` with a REST or GUI layer by reusing services and DTOs.
- Expand `ValidationUtils` to include configurable rules (properties or feature flags) if needed.

## Implementation Notes (files)
- `ConsoleMenu.java`:
    - Provide read-loop helpers and descriptive prompts.
    - Use `ValidationUtils` for input validation.
    - Use service queries to validate existence (`readExistingProjectId`, `readExistingUserId`).
- `ValidationUtils.java`:
    - Keep static validation sets and `EMAIL_PATTERN`.
    - Expose boolean validators + helper messages (either constants or methods returning allowed values).
- `Main.java`:
    - Compose services and inject them into `ConsoleMenu`.
    - Prefer explicit `setXService(...)` methods as shown in UML.

## Mermaid / Documentation Note
- Keep Mermaid diagrams with one statement per line and correct frontmatter. Example: `---` frontmatter block, then `classDiagram` and separate `note` and `class` lines to avoid parse errors.

## Risks & Mitigations
- Risk: Business logic creeping into `ConsoleMenu`.
    - Mitigation: Enforce rule in code reviews and add tests covering services only.
- Risk: Inconsistent validation messages across CLI.
    - Mitigation: Store canonical allowed-values strings in `ValidationUtils` and reuse them in prompts.

## Quick Decision Checklist
1. Keep services pure and testable — yes.
2. Centralize validation — yes (`ValidationUtils`).
3. Use ID-based associations — yes.
4. Keep CLI thin and user-friendly — yes.
5. Prepare for future persistence by introducing repository interfaces when needed — recommended.

## Relevant files
- `ConsoleMenu.java` — CLI & input helpers.
- `ValidationUtils.java` — validation rules.
- `UserService.java`, `ProjectService.java`, `TaskService.java`, `ReportService.java` — business logic.
- `Main.java` — wiring and bootstrap.
- `uml_mermaid_fixed.mmd` — architecture reference diagram.
