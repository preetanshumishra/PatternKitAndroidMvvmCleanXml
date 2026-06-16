package com.preetanshumishra.patternkit.android.mvvmcleanxml.domain

enum class TaskFilter(val displayName: String) {
    ALL("All"),
    ACTIVE("Active"),
    COMPLETED("Completed");

    fun matches(task: TaskItem): Boolean = when (this) {
        ALL       -> true
        ACTIVE    -> !task.isCompleted
        COMPLETED -> task.isCompleted
    }
}
