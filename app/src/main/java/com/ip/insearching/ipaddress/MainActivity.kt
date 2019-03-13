package com.ip.insearching.ipaddress

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log


class MainActivity : AppCompatActivity() {

    companion object {
        private const val JOB_ID = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceName = ComponentName(this, JobSchedulerService::class.java)

        val jobInfo = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .build()

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val result = scheduler.schedule(jobInfo)
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d("MY_TAG", "Job scheduled successfully!")
        }

        finish()
    }
}
