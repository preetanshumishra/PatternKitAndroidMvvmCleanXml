package com.preetanshumishra.patternkit.android.mvvmcleanxml.di

import com.preetanshumishra.patternkit.android.mvvmcleanxml.domain.CreateTaskUseCase
import com.preetanshumishra.patternkit.android.mvvmcleanxml.domain.DeleteTaskUseCase
import com.preetanshumishra.patternkit.android.mvvmcleanxml.domain.GetTasksUseCase
import com.preetanshumishra.patternkit.android.mvvmcleanxml.domain.ToggleTaskCompletionUseCase
import com.preetanshumishra.patternkit.android.mvvmcleanxml.domain.UpdateTaskUseCase
import dagger.Component
import javax.inject.Singleton

/**
 * Application-scoped Dagger graph. ViewModels aren't `@Inject`-built
 * directly (they need lifecycle-aware construction), so the component
 * exposes the use cases they depend on — the hand-rolled `ViewModelFactory`
 * in the ui layer composes them. (Contrast the plain MVVM-XML sibling, where
 * the component exposed only the repository.)
 */
@Singleton
@Component(modules = [RepositoryModule::class])
interface AppComponent {
    fun getTasksUseCase(): GetTasksUseCase
    fun createTaskUseCase(): CreateTaskUseCase
    fun updateTaskUseCase(): UpdateTaskUseCase
    fun deleteTaskUseCase(): DeleteTaskUseCase
    fun toggleTaskCompletionUseCase(): ToggleTaskCompletionUseCase
}
