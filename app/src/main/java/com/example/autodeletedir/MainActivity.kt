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

        // 检查是否有写外部存储权限
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 尝试删除目录
            deleteDirectory(directoryPath) { result ->
                // 更新删除操作的结果状态
                deletionResult = result
            }
        }

        // 根据删除操作的结果显示不同的文本内容
        Text(
            text = deletionResult ?: "操作结果未知",
            modifier = Modifier.fillMaxSize()
        )
    }

    // 异步删除目录的函数
    private fun deleteDirectory(directoryPath: String, onComplete: (String) -> Unit) {
        val directory = File(directoryPath)

        if (directory.exists() && directory.isDirectory) {
            val deleted = deleteRecursively(directory)
            if (deleted) {
                // 调用 onComplete 回调，通知删除成功
                onComplete("删除成功")
            } else {
                // 调用 onComplete 回调，通知删除失败
                onComplete("删除失败")
            }
        } else {
            // 调用 onComplete 回调，通知未找到目录
            onComplete("没有找到目录")
        }
    }

    // 递归删除目录及其内容的函数
    private fun deleteRecursively(fileOrDirectory: File): Boolean {
        if (fileOrDirectory.isDirectory) {
            val children = fileOrDirectory.listFiles()
            if (children != null) {
                for (child in children) {
                    val success = deleteRecursively(child)
                    if (!success) {
                        return false
                    }
                }
            }
        }
        return fileOrDirectory.delete()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AutoDeleteDirTheme {
        Text("Preview")
    }
}
