# AI Development Prompts Documentation

## Project: AI-Semantic Log Viewer

---

## Overview

This document provides transparency into the usage of AI tools during the development of the **AI-Driven Semantic Log Viewer** application.

AI tools were used as development assistants to accelerate implementation, improve productivity, explore solutions, and validate engineering decisions.

The following AI tools were used:

- Cursor AI
- GitHub Copilot

All AI-generated suggestions were reviewed, modified, tested, and adapted according to project requirements.

AI was used to assist development, but all final architecture decisions, implementation choices, and validations were performed manually.

For setup instructions, architecture overview, and feature list, see [README.md](README.md).

---

# Cursor AI Usage

## 1. Application Architecture Design

### Goal

Define a scalable Android architecture following modern Android development practices.

### Prompt Used

```
Design a scalable Android application architecture for an AI-driven log viewer.

Requirements:

- Kotlin
- Jetpack Compose
- Clean Architecture
- MVI pattern
- Hilt dependency injection
- Retrofit API integration
- Room database caching
- Unit testing support
- Handle large log datasets
```

### AI Assistance

Cursor helped evaluate:

- Clean Architecture layer separation
- MVI state management
- Repository responsibilities
- Data flow between layers
- Testability considerations

### Final Implementation

Implemented a **layered architecture inspired by Clean Architecture**:

```
Presentation Layer

Compose UI
     |
     |
ViewModel
     |
     |
LogIntent + LogState


Domain Layer

UseCase
     |
     |
Repository Interface (LogRepository)


Data Layer

Repository Implementation (LogRepositoryImpl)

     |
     |----------------|
     |                |

 Retrofit API      Room Database
```

The repository interface lives in the domain layer; the implementation lives in `data/repository/`. `GetLogsUseCase` is intentionally thin — it delegates directly to the repository.

**Note:** The original prompt requested support for large log datasets. The current implementation loads all logs at once with no pagination (see [Known Limitations](#known-limitations)).

---

## 2. Repository Cache-First Strategy

### Goal

Implement local caching with Room and remote synchronization.

### Prompt Used

```
Implement repository logic using Room and Retrofit.

Requirements:

1. Load cached data from Room first.
2. Display cached data immediately.
3. Fetch latest data from API.
4. Update Room database.
5. Notify UI using Kotlin Flow.
```

### AI Assistance

Cursor suggested:

- Room as local source of truth
- Flow based reactive updates
- Repository coordinating multiple data sources
- Offline handling strategy

### Final Implementation

`LogRepositoryImpl` in `data/repository/` follows this flow:

```
Application Start

       |
       ↓

Repository

       |
       ↓

Read Room Cache (one-shot read)

       |
       ↓

Emit Cached Logs (if non-empty)

       |
       ↓

Fetch Remote Data

       |
       ↓

Update Database

       |
       ↓

Emit Updated Logs
```

On API failure: if cached data exists, it remains visible; if cache is empty, a failure is emitted.

The repository uses a one-shot `dao.getLogs().first()` per fetch cycle rather than continuous Room observation.

---

## 3. Retrofit and Networking Setup

### Goal

Create clean networking layer.

### Prompt Used

```
Create Retrofit API integration using Kotlin.

Requirements:

- Hilt dependency injection
- Suspend functions
- DTO models
- Error handling
- Clean architecture separation
```

### AI Assistance

Helped create:

- Retrofit service structure
- Network dependency injection
- DTO mapping approach

### Final Implementation

Created:

```
LogApiService

LogResponseDto (+ LogDto nested inside)

MetadataDto

ApiConstants

NetworkModule

LogMapper (DTO ↔ Domain ↔ Entity)
```

---

## 4. Jetpack Compose UI Components

### Goal

Build reusable Material 3 UI components.

### Prompt Used

```
Create Jetpack Compose components for a log viewer.

Components:

- Log list item
- Severity indicator
- Search bar
- Details bottom sheet
- Loading shimmer

Follow Material 3 guidelines.
```
## 5. Custom Severity Indicator

### Goal

Create a visual representation of log severity.

### Prompt Used

```
Create a reusable Jetpack Compose component that visually represents log severity.

Support:

- INFO
- WARNING
- ERROR

Use Material 3 design principles.
```

### AI Assistance

Provided suggestions for:

- Component API design
- Canvas based visualization ideas
- UI improvements

### Final Implementation

Created `SeverityIndicator()` with a matching `SeverityBadge()` in the detail sheet. Both handle `WARN` and `WARNING` severity values consistently.

Used in:

- Log list (`LogItem`)
- Log details sheet (`LogDetailBottomSheet`)

---
## 6. Search Functionality

### Goal

Implement a responsive search-as-you-type experience for a large dataset while keeping the UI smooth and avoiding unnecessary filtering.

### Prompt Used

```
How can I implement an efficient search-as-you-type feature in Kotlin Coroutines using Flow that filters 5,000+ log entries by message, tag, and severity with debounce and minimal UI updates?
```

### AI Assistance

Suggested:

- Using `MutableStateFlow` for search state.
- Applying `debounce(300)` to reduce unnecessary filtering.
- Using `distinctUntilChanged()` to prevent duplicate searches.
- Performing in-memory filtering instead of making additional API requests.
- Updating UI state after filtering.

### Manual Refinement

The implementation was adapted to match the project's MVI architecture by:

- Keeping the complete log dataset inside the `ViewModel`.
- Filtering logs in memory.
- Supporting searches by:
    - Message
    - Tag
    - Severity
- Updating the UI through a centralized `updateUiState()` function.

### Final Implementation

```
User Input
     │
     ▼
MutableStateFlow
     │
     ▼
debounce(300ms)
     │
     ▼
distinctUntilChanged()
     │
     ▼
Filter In-Memory Logs
     │
     ▼
updateUiState()
     │
     ▼
Compose UI
```

The current implementation performs client-side filtering on the in-memory dataset held by the `ViewModel`. No additional Room queries or network requests are performed during search.
---

## 7. ViewModel State Management

### Goal

Improve ViewModel maintainability using MVI principles.

### Prompt Used

```
Review this ViewModel implementation.

Improve:

- State handling
- Intent processing
- StateFlow usage
- Testability
```

### AI Assistance

Suggested:

- Centralized state update function
- Immutable UI state
- Intent-driven actions

### Final Implementation

Implemented:

```
LogIntent

    ↓

LogViewModel

    ↓

LogState

    ↓

Compose UI
```

with centralized:

```
updateUiState()
```

---

## 8. Unit Testing Assistance

### Goal

Create unit tests for the repository, business logic, and ViewModel state.

### Prompt Used

```
Generate unit tests for Android Repository, ViewModel, and UseCase.

Use:

- JUnit
- MockK
- Kotlin Coroutine Test
- Turbine

Test:

- Cached data
- API success
- API failure
- Loading state
- Success state
- Error state
- Search filtering
- Bottom sheet state
```

### AI Assistance

Helped generate:

- Test class structure
- Coroutine test setup
- MockK configuration
- Flow testing with Turbine
- Repository mocking patterns
- Assertion patterns for state and Flow

### Final Implementation

Tests added for:

#### Repository (LogRepositoryImplTest)

Uses *MockK*, *kotlinx-coroutines-test*, and *Turbine* to verify repository behavior:

- Returns cached logs immediately when available
- Fetches latest logs from the API
- Saves remote logs into Room database
- Emits updated logs after a successful API response
- Emits an error when the API request fails and no cache exists
- Does not emit an error when cached data is already available and the API request fails

#### ViewModel (LogViewModelTest)

Uses *MockK* and *kotlinx-coroutines-test* with direct state.value assertions:

- Loading state while fetching
- Load logs success
- Load logs failure
- Search filtering
- Empty search restores all logs
- Detail sheet open/close

#### UseCase (GetLogsUseCaseTest)

Uses *MockK*, *kotlinx-coroutines-test*, and *Turbine* for Flow assertions:

- Repository delegation
- Success result
- Failure result

*Not yet covered:* instrumentation/UI tests (Compose UI tests), end-to-end integration tests, and performance benchmarking beyond the default Android Studio template.

---

# GitHub Copilot Usage

## 1. Kotlin Code Generation

### Prompt / Context

```
Generate Kotlin boilerplate code for:

- Data classes
- Mapper functions
- Repository interfaces
- Extension functions
```

### AI Assistance

Used for:

- Faster Kotlin development
- Reducing repetitive code
- Syntax suggestions

### Final Implementation

Created:

```
Log.kt (domain model)

LogDto (nested in LogResponseDto.kt)

LogEntity.kt

LogMapper.kt (mapping extensions)
```

---

## 2. Room Database Implementation

### Prompt / Context

```
Create Room database implementation.

Requirements:

- Entity
- DAO
- Database class
- Hilt provider
```

### AI Assistance

Helped generate:

- DAO structure
- Query methods
- Database configuration

### Final Implementation

Created:

```
LogEntity

LogDao

AppDatabase

DatabaseModule
```

---

## 3. Compose Code Completion

### Prompt / Context

```
Complete this Jetpack Compose UI component.

Requirements:

- Material 3
- Proper spacing
- Reusable structure
```

### AI Assistance

Helped with:

- Modifier suggestions
- Layout improvements
- Component structure

### Final Implementation

Created reusable Compose components (see section 4 above for the full component table).

---

## 4. Documentation Assistance

### Prompt / Context

```
Create professional documentation for an Android project.

Include:

- Setup instructions
- Architecture explanation
- Features
- Testing strategy
- Demo section
```

### AI Assistance

Helped structure:

- README.md
- PROMPTS.md
- Technical explanations

### Final Implementation

- [README.md](README.md) — project overview, setup, architecture, testing, known limitations
- [PROMPTS.md](PROMPTS.md) — this transparency document

---

# AI Usage Guidelines Followed

During development:

- AI suggestions were reviewed before implementation.
- Generated code was modified when required.
- No sensitive production information was shared.
- Architecture decisions were evaluated manually.
- All implemented features were tested.

---

# Known Limitations

These are intentional scope boundaries or areas not yet implemented:

| Area | Current state |
|---|---|
| Large datasets | All logs loaded at once; no Paging 3 or pagination |
| Test coverage | Compose UI tests |

---

# Summary

AI tools helped accelerate:

| Area | Tool Used |
|---|---|
| Architecture discussion | Cursor AI |
| Compose development | Cursor AI + GitHub Copilot |
| Kotlin coding | GitHub Copilot |
| Repository design | Cursor AI |
| Unit testing | Cursor AI |
| Documentation | Cursor AI + GitHub Copilot |
| Code review | Cursor AI |

Final implementation, testing, and engineering decisions were completed manually.
