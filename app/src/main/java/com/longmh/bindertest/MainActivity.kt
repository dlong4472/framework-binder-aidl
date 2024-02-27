package com.longmh.bindertest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Runnable

/**
 * Main Activity
 */
class MainActivity : ComponentActivity() {

    companion object {
        val TAG = "TestBinderMainActivity"
    }

    /**
     * IMyAidlInterface
     */
    var mIMyAidlInterface: IMyAidlInterface? = null

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColumnLayout(
                // 启动服务
                {
                    startService(Intent(this, TestAidlService::class.java))
                },
                // 绑定服务
                {
                    bindService(
                        Intent(this@MainActivity, TestAidlService::class.java),
                        connection,
                        Context.BIND_AUTO_CREATE
                    )
                },
                // 获取数据
                {
                    mIMyAidlInterface?.let {
                        Log.d(TAG, "getData 1: ${it.data}")
                        Log.d(TAG, "setData : 123")
                        it.data = 123
                        Log.d(TAG, "getData 2: ${it.data}")
                    }
                })
        }
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e(TAG, "onServiceConnected: success")
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service) // proxy
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e(TAG, "onServiceDisconnected: success")
            mIMyAidlInterface = null
        }
    }

    /**
     * onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}

/**
 * Preview
 */
@Composable
fun ColumnLayout(startService: Runnable, bindService: Runnable, getData: Runnable) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                startService.run()
            },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "启动服务")
        }
        Button(
            onClick = { bindService.run() },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "绑定服务")
        }
        Button(
            onClick = { getData.run() },
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "获取数据")
        }
    }
}