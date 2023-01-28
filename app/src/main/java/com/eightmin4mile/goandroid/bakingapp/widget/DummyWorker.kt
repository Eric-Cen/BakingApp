package com.eightmin4mile.goandroid.bakingapp.widget

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class DummyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val TAG = "TAG_DummyWorker"

        @JvmStatic
        fun schedule(context: Context) {
            val dummyWorker = OneTimeWorkRequest.Builder(DummyWorker::class.java)
                .addTag(TAG)
                .setInitialDelay(10L * 365L, TimeUnit.DAYS)
                .build()
            WorkManager.getInstance(context)
                .beginWith(dummyWorker)
                .enqueue()
        }

        @JvmStatic
        fun remove(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
        }
    }

    override fun doWork(): Result {
        return Result.success()
    }
}
