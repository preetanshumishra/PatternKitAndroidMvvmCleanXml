package com.preetanshumishra.patternkit.android.mvvmcleanxml.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.preetanshumishra.patternkit.android.mvvmcleanxml.di.AppComponent
import com.preetanshumishra.patternkit.android.mvvmcleanxml.ui.list.TaskListViewModel
import com.preetanshumishra.patternkit.android.mvvmcleanxml.ui.form.TaskFormViewModel
import com.preetanshumishra.patternkit.android.mvvmcleanxml.ui.form.TaskFormMode

/**
 * Hand-rolled VM factory — explicit composition root for the UI layer.
 * Pulls the use cases out of the Dagger graph and hands them to each
 * ViewModel. (Contrast the plain MVVM-XML sibling, which injected the
 * repository here instead — the use-case layer is the Clean difference.)
 */
class ViewModelFactory(
    private val component: AppComponent,
    private val formMode: TaskFormMode? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(TaskListViewModel::class.java) ->
                TaskListViewModel(
                    getTasks = component.getTasksUseCase(),
                    deleteTask = component.deleteTaskUseCase(),
                    toggleTaskCompletion = component.toggleTaskCompletionUseCase()
                ) as T

            modelClass.isAssignableFrom(TaskFormViewModel::class.java) ->
                TaskFormViewModel(
                    mode = formMode ?: TaskFormMode.Create,
                    createTask = component.createTaskUseCase(),
                    updateTask = component.updateTaskUseCase()
                ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        /** Convenience for screens that don't need a form-mode argument. */
        fun forList(component: AppComponent) = ViewModelFactory(component)

        /** Convenience for form screens, supplying the create-vs-edit mode. */
        fun forForm(component: AppComponent, mode: TaskFormMode) =
            ViewModelFactory(component, mode)
    }
}
