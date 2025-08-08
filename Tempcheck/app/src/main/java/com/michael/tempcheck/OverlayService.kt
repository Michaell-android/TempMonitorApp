package com.michael.tempcheck

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Choreographer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import java.io.File

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private val handler = Handler(Looper.getMainLooper())

    private var cpuZone: String? = null
    private var gpuZone: String? = null

    private var frameCount = 0
    private var lastTimeNs = 0L

    private val updateRunnable = object : Runnable {
        override fun run() {
            updateCpuTemp()
            updateGpuTemp()
            handler.postDelayed(this, 1000) // temps update every 1 second
        }
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (lastTimeNs > 0) {
                frameCount++
                val diffNs = frameTimeNanos - lastTimeNs
                if (diffNs >= 1_000_000_000L) { // 1 second
                    val fps = frameCount
                    overlayView.findViewById<TextView>(R.id.fps).text = "FPS: $fps"
                    frameCount = 0
                    lastTimeNs = frameTimeNanos
                }
            } else {
                lastTimeNs = frameTimeNanos
            }
            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Detect CPU & GPU thermal zones at startup
        detectThermalZones()

        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, params)

        // Start temp updates
        handler.post(updateRunnable)

        // Start FPS tracking
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    private fun detectThermalZones() {
        val basePath = "/sys/class/thermal/"
        File(basePath).listFiles()?.forEach { zone ->
            val typeFile = File(zone, "type")
            val tempFile = File(zone, "temp")
            if (typeFile.exists() && tempFile.exists()) {
                val type = typeFile.readText().trim().lowercase()
                when {
                    cpuZone == null && ("cpu" in type || "soc" in type) -> {
                        cpuZone = tempFile.absolutePath
                    }
                    gpuZone == null && "gpu" in type -> {
                        gpuZone = tempFile.absolutePath
                    }
                }
            }
        }
    }

    private fun updateCpuTemp() {
        val cpuTempView = overlayView.findViewById<TextView>(R.id.cpu_temp)
        val temp = cpuZone?.let { readTempFromFile(it) }
        cpuTempView.text = "CPU: ${temp ?: "--"}°C"
    }

    private fun updateGpuTemp() {
        val gpuTempView = overlayView.findViewById<TextView>(R.id.gpu_temp)
        val temp = gpuZone?.let { readTempFromFile(it) }
        gpuTempView.text = "GPU: ${temp ?: "--"}°C"
    }

    private fun readTempFromFile(path: String): String? {
        return try {
            val text = File(path).readText().trim()
            val value = text.toFloatOrNull()
            if (value != null) {
                if (value > 1000) (value / 1000f).toString() else value.toString()
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
        Choreographer.getInstance().removeFrameCallback(frameCallback)
        if (::windowManager.isInitialized && ::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
