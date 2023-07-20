package com.example.envintiatestapp.rules

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class LogRule : TestRule {
    @Throws(Throwable::class)
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                mockkStatic(Log::class)
                every { Log.i(any(), any()) }.returns(0)
                every { Log.e(any(), any()) }.returns(0)

                try {
                    base.evaluate()
                } finally {
                    //TODO reset logger
                }
            }

        }
    }
}