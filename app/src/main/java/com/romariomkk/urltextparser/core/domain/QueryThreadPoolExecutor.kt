package com.romariomkk.urltextparser.core.domain

import java.util.concurrent.*
import java.util.concurrent.locks.ReentrantLock

class QueryThreadPoolExecutor(
    corePoolSize: Int, maxPoolSize: Int,
    keepAliveTime: Long, timeUnit: TimeUnit,
    blockingQueue: BlockingQueue<Runnable>,
    threadFactory: ThreadFactory
): ThreadPoolExecutor(
    corePoolSize, maxPoolSize,
    keepAliveTime, timeUnit,
    blockingQueue, threadFactory
) {

    var isPaused: Boolean = false
    private val pauseLock = ReentrantLock()
    private val lockObject = pauseLock.newCondition()

    override fun beforeExecute(thread: Thread?, runnable: Runnable?) {
        super.beforeExecute(thread, runnable)
        pauseLock.lock()
        try {
            while (isPaused) {
                lockObject.await()
            }
        } catch (exc: InterruptedException) {
            thread?.interrupt()
        } finally {
            pauseLock.unlock()
        }
    }

    fun switchPauseResume() {
        if (isPaused) resume() else pause()
    }

    fun forceResume() {
        resume()
    }

    private fun resume() {
        pauseLock.lock()
        try {
            isPaused = false
            lockObject.signalAll()
        } finally {
            pauseLock.unlock()
        }
    }

    private fun pause() {
        pauseLock.lock()
        try {
            isPaused = true
        } finally {
            pauseLock.unlock()
        }
    }

    fun clearTaskQueue() {
        queue.clear()
    }
}