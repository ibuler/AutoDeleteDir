package com.example.autodeletedir

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.autodeletedir.ui.theme.AutoDeleteDirTheme
import java.io.File
import com.stericson.RootTools.RootTools
import com.stericson.RootShell.execution.Command

class MainActivity : ComponentActivity() {

    private val directoryPath = "/data/local/cfg-znz"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutoDeleteDirTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // 在 @Composable 函数中调用 DeleteDirectoryMessage
                    DeleteDirectoryMessage(directoryPath)
                }
            }
        }
    }

    @Composable
    private fun DeleteDirectoryMessage(directoryPath: String) {
        // 定义删除操作的结果状态
        var deletionResult by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            if (RootTools.isRootAvailable() && RootTools.isAccessGiven()) {
                val command = Command(0, "rm -r $directoryPath")
                RootTools.getShell(true).add(command)
                deletionResult = "删除成功"
            } else {
                deletionResult = "设备未获得 root 权限或无法执行 root 命令"
            }
        }

        // 根据删除操作的结果显示不同的文本内容
        Text(
            text = deletionResult ?: "操作结果未知",
            modifier = Modifier.fillMaxSize()
        )
    }
}

