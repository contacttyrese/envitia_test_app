package com.example.envintiatestapp.rules

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule : TestRule {
    private val scheduler = mockk<Scheduler>()
    private val worker = mockk<Scheduler.Worker>()
    private val disposable = mockk<Disposable>()
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                mockkStatic(Schedulers::class)
                RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
                every { Schedulers.trampoline() }.returns(scheduler)
                every { Schedulers.io() }.returns(scheduler)
                every { scheduler.scheduleDirect(any()) }.returns(disposable)
                every { scheduler.createWorker() }.returns(worker)

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }

        }
    }
}