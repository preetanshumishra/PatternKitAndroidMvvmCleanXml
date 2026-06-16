package com.preetanshumishra.patternkit.android.mvvmcleanxml.domain

import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * Use cases — one verb each, the domain layer's public API to the UI.
 * Their job is to express "what the app does" without leaking how it's
 * stored or rendered. ViewModels depend on use cases, not on the
 * repository directly — that's the Clean layering vs. the plain MVVM-XML
 * sibling (where the ViewModel called the repository itself).
 */

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): List<TaskItem> = repository.fetchAll()
}

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: TaskItem): TaskItem = repository.create(task)
}

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: TaskItem): TaskItem =
        repository.update(task.copy(updatedAt = Instant.now()))
}

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: UUID) = repository.delete(id)
}

/**
 * Composite use case: flipping completion is a domain concept, not a
 * dumb pass-through. Keeping it here means the ViewModel can't accidentally
 * forget to bump `updatedAt`.
 */
class ToggleTaskCompletionUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: TaskItem): TaskItem =
        repository.update(task.copy(isCompleted = !task.isCompleted, updatedAt = Instant.now()))
}
