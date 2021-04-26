package com.example.cardevice

import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cbsd.YuvTools
import com.cbsd.YuvUtils
import me.goldze.mvvmhabit.utils.KLog
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.cardevice", appContext.packageName)
        appContext
    }


    @Test
    fun checkBmp() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val bmp = BitmapFactory.decodeResource(appContext.resources, R.drawable.this_icon)

        val desWidth = bmp.width
        val desHeight = bmp.height
        val src_nv21: ByteArray = YuvTools.getNV21(desWidth, desHeight, bmp)
        val src_i420 = ByteArray(desWidth * desHeight * 3 / 2)
        val result = ByteArray(desWidth * desHeight * 3 / 2)
        YuvUtils.yuvNV21ToI420(src_nv21, desWidth, desHeight, src_i420)

        YuvUtils.yuvI420ToNV21(src_i420, desWidth, desHeight, result)
        val bitmap = YuvTools.yuv2Bitmap(result, desWidth, desHeight)

        KLog.e("asdas", "dasdadaad")

    }
}