package wang.relish.widget.sample.sample.surface

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

/**
 * 线程池
 *
 * @author relish
 * @since 20170802
 */
/* package */ internal enum class ThreadPool(threadCount: Int, name: String, isStack: Boolean) {

    SINGLE(1, "Single Task", false),
    DRAW(10, "Draw Task", false);
    // db IO，必须为单线程线程池


    private val mExecutor: ThreadPoolExecutor

    /* package */  val executorService: ExecutorService
        get() = mExecutor

    init {
        val queue: BlockingQueue<Runnable>
        if (isStack) {
            queue = LinkedBlockingStack()
        } else {
            queue = LinkedBlockingQueue()
        }

        mExecutor = ThreadPoolExecutor(
            threadCount,
            threadCount,
            30,
            TimeUnit.SECONDS,
            queue,
            ThreadFactory { r -> Thread(r, name) })

        mExecutor.allowCoreThreadTimeOut(true)
    }

    fun execute(runnable: Runnable): Future<*> {
        return mExecutor.submit(RunnableTask(runnable))
    }


    private class RunnableTask internal constructor(private val mRunnable: Runnable) : Runnable {

        init {
            requireNotNull(mRunnable) { "runnable is null!" }
        }

        override fun run() {
            try {
                mRunnable.run()
            } catch (t: Throwable) {
            }

        }

        companion object {

            private val HANDLER = Handler(Looper.getMainLooper())
        }
    }

    private class LinkedBlockingStack<T> : LinkedBlockingDeque<T>() {
        override fun add(t: T): Boolean {
            super.addFirst(t)
            return true
        }

        override fun offer(t: T): Boolean {
            return super.offerFirst(t)
        }

        @Throws(InterruptedException::class)
        override fun put(t: T) {
            super.putFirst(t)
        }

        @Throws(InterruptedException::class)
        override fun offer(t: T, timeout: Long, unit: TimeUnit): Boolean {
            return super.offerFirst(t, timeout, unit)
        }
    }
}