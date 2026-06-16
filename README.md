# PatternKit — MVVM + Clean Architecture (XML Views)

Part of **PatternKit**, a side-by-side reference codebase where the same small **Tasks** CRUD app is implemented once per architecture pattern across iOS and Android. Every module ships identical behaviour — the same domain model, the same three screens, the same mock data layer — so the only thing that varies is the architecture itself.

This module is the **MVVM + Clean Architecture** flavour on the **traditional XML view stack** — Fragments, the Navigation component, ViewBinding, and a `RecyclerView` adapter. It keeps MVVM in the presentation layer but interposes a **domain layer of use cases** between the ViewModels and the data layer. It's the XML counterpart to the MVVM-Clean-Compose module, and the direct Clean-axis sibling of the plain MVVM-XML module: same UI stack, but with the domain layer added.

## Stack

- **Language:** Kotlin
- **UI:** XML layouts + ViewBinding · Fragments · `RecyclerView`
- **Architecture:** Clean Architecture (Domain / Data / UI) with MVVM presentation (LiveData)
- **Navigation:** Jetpack Navigation component (`nav_graph.xml`)
- **DI:** Dagger 2 (KSP) — plain Dagger, no Hilt
- **Min SDK:** 28 · **Target/Compile SDK:** 36
- **Package:** `com.preetanshumishra.patternkit.android.mvvmcleanxml`

## The Tasks feature

A single-user task list. One entity (`TaskItem`: title, optional notes, optional due date, priority, completion). Three screens:

1. **List** (`TaskListFragment`) — filter chips (All / Active / Completed), sort by due date or priority, swipe-to-delete via `RecyclerView`, FAB to create.
2. **Detail** (`TaskDetailFragment`) — read-only fields, toggle completion, edit, delete.
3. **Form** (`TaskFormFragment`) — create or edit (mode-driven), title validation (≤ 80 chars), due-date validation (not in the past), 600 ms mock async save.

Data comes from `MockTaskRepository` — an in-memory store seeded with ~12 tasks, with configurable artificial latency and failure rate. No real network, no local persistence — intentionally, so the architecture stays the focus.

## The domain layer (what makes this "Clean")

Five single-responsibility use cases sit between the presentation and data layers (`UseCases.kt`):

- `GetTasksUseCase`, `CreateTaskUseCase`, `UpdateTaskUseCase`, `DeleteTaskUseCase`, `ToggleTaskCompletionUseCase`

Each depends only on the `TaskRepository` contract (declared in `domain/`). ViewModels compose use cases rather than touching the repository directly — the one structural difference from the plain MVVM-XML module. The completion-toggle and `updatedAt`-stamp rules that lived in the MVVM-XML ViewModels now live in their use cases, reusable and unit-testable in isolation from any UI.

## XML-stack specifics

- **Fragments + Navigation component** — screens are Fragments wired through `res/navigation/nav_graph.xml`; arguments are passed via a small `NavArgs` helper.
- **ViewBinding** — type-safe view access, no `findViewById`.
- **`RecyclerView` + `TaskAdapter`** — list rendering with a diffing adapter.
- **`Event` wrapper** — one-shot UI events (navigation, errors) exposed from the ViewModel without re-firing on configuration change.

## Dependency injection

Plain Dagger 2, wired by hand:

- `AppComponent` (`@Singleton`) exposes the five use cases; `RepositoryModule` `@Binds` `TaskRepository` → `MockTaskRepository`.
- `PatternKitApp` creates and holds the component; a hand-rolled `ViewModelFactory` pulls the use cases out of the graph and constructs each ViewModel.

## Project layout

```
app/src/main/
├── kotlin/.../mvvmcleanxml/
│   ├── domain/      # TaskItem, Priority, TaskFilter, TaskSort,
│   │               #   TaskRepository (contract), UseCases.kt
│   ├── data/        # MockTaskRepository, seed data
│   ├── di/          # AppComponent, RepositoryModule
│   ├── ui/
│   │   ├── list/    # TaskListFragment + TaskAdapter + TaskListViewModel
│   │   ├── detail/  # TaskDetailFragment
│   │   ├── form/    # TaskFormFragment + TaskFormViewModel
│   │   ├── Event.kt, NavArgs.kt, MainActivity, ViewModelFactory
│   └── PatternKitApp.kt
└── res/
    ├── layout/      # activity_main, fragment_list/detail/form, item_task
    ├── menu/        # menu_list, menu_detail
    └── navigation/  # nav_graph.xml
```

## Build & run

```bash
./gradlew assembleDebug      # build the debug APK
./gradlew installDebug       # install on a connected device/emulator
./gradlew test               # unit tests
```

Or open the project in Android Studio and run the `app` configuration.
