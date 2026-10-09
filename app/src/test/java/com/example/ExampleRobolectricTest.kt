package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.features.DefaultFeaturesCatalog
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("BOT GENERATOR", appName)
    }

    @Test
    fun `verify 350 features generated`() {
        val features = DefaultFeaturesCatalog.createInitial350Features()
        assertEquals(350, features.size)
        assertEquals(1, features.first().id)
        assertEquals(350, features.last().id)
    }
}
