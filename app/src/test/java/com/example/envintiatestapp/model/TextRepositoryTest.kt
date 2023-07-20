package com.example.envintiatestapp.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.envintiatestapp.rules.LogRule
import com.example.envintiatestapp.rules.RxSchedulerRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import java.io.File

class TextRepositoryTest {
    @get:Rule
    val logRule = LogRule()
    @get:Rule
    val schedulerRule = RxSchedulerRule()
    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    private val file = mockk<File>(relaxed = true)
    private lateinit var repository: TextRepository

    @Before
    fun setUp() {
        every { file.exists() }.returns(true)
        every { file.canonicalPath }.returns("testPath")
        every { file.path }.returns("testPath")
        every { file.createNewFile() }.returns(true)

        repository = TextRepository(file)
    }

    @Test
    fun `GIVEN capturedText is valid WHEN save text THEN capturedText is saved`() {
        val testText = CapturedText("12:00 pm", "red")
        val actual = repository.saveTextToJson(testText)

        assertTrue("file did not successfully save", actual)
    }

    @Test
    fun `GIVEN text in capturedText is empty WHEN save text THEN capturedText is not saved`() {
        val testText = CapturedText("12:00 pm", "")
        val actual = repository.saveTextToJson(testText)

        assertFalse("file did successfully save", actual)
    }

    @Test
    fun `GIVEN text in capturedText is blank WHEN save text THEN capturedText is not saved`() {
        val testText = CapturedText("12:00 pm", " ")
        val actual = repository.saveTextToJson(testText)

        assertFalse("file did successfully save", actual)
    }

    @Test
    fun `GIVEN time in capturedText is empty WHEN save text THEN capturedText is not saved`() {
        val testText = CapturedText("", "red")
        val actual = repository.saveTextToJson(testText)

        assertFalse("file did successfully save", actual)
    }

    @Test
    fun `GIVEN time in capturedText is blank WHEN save text THEN capturedText is not saved`() {
        val testText = CapturedText("", "red")
        val actual = repository.saveTextToJson(testText)

        assertFalse("file did successfully save", actual)
    }

    @Test
    fun `GIVEN time in capturedText is incorrect format WHEN save text THEN capturedText is not saved`() {
        val testText = CapturedText("082313dasdas", "red")
        val actual = repository.saveTextToJson(testText)

        assertFalse("file did successfully save", actual)
    }

    @Test
    fun `GIVEN file is valid WHEN loadTexts THEN observable is not null`() {
        val actual = repository.getLoadedTexts().test()

        actual.assertSubscribed()
        actual.assertNoErrors()

    }
}