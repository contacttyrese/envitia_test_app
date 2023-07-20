package com.example.envintiatestapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.envintiatestapp.model.TextRepository
import com.example.envintiatestapp.rules.LogRule
import com.example.envintiatestapp.rules.RxSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.disposables.CompositeDisposable

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val logRule = LogRule()
    @get:Rule
    val schedulerRule = RxSchedulerRule()
    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    private val repository = mockk<TextRepository>()
    private val disposables = mockk<CompositeDisposable>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = spyk(MainViewModel(repository, disposables), recordPrivateCalls = true)
    }

    @Test
    fun `GIVEN disposables is valid WHEN view model init THEN add disposable is called`() {
        every { disposables.add(any()) }.returns(true)
        verify { disposables.add(any()) }
    }
}