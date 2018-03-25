package net.useobjects.workingtime.utility

import android.os.Handler
import android.os.Looper
import java.util.concurrent.TimeUnit

class GuiThreadTimer(private val period: Long,
                     private val timeUnit: TimeUnit,
                     private val task: Runnable) {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val taskRunner: TaskRunner = TaskRunner()
    private var isRunning: Boolean = false

    fun start() {
        handler.post(taskRunner)
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }

    private inner class TaskRunner : Runnable {
        override fun run() {
            if (isRunning) {
                try {
                    task.run()
                } finally {
                    handler.postDelayed(taskRunner, timeUnit.toMillis(period))
                }
            }
        }
    }
}