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

        // 在 setContent 中设置界面
        setContent {
            AutoDeleteDirTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // 定义并初始化 deletionResult 变量
                    val deletionResult = remember { mutableStateOf<String?>(null) }
                    // 调用 DeleteDirectoryMessage 显示界面
                    DeleteDirectoryMessage(directoryPath, deletionResult)
                }
            }
        }

        // 检查是否有写外部存储权限
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 尝试删除目录
            deleteDirectory(directoryPath)
        }
    }

    // 删除目录的函数
    private fun deleteDirectory(directoryPath: String) {
        val directory = File(directoryPath)
        val deletionResult = mutableStateOf<String?>(null)

        // 检查目录是否存在且是一个目录
        if (directory.exists() && directory.isDirectory) {
            // 尝试递归删除目录及其内容
            val deleted = deleteRecursively(directory)
            if (deleted) {
                // 更新 deletionResult 的值为 "删除成功"
                deletionResult.value = "删除成功"
            } else {
                // 更新 deletionResult 的值为 "删除失败"
                deletionResult.value = "删除失败"
            }
        } else {
            // 更新 deletionResult 的值为 "没有发现这个目录"
            deletionResult.value = "没有发现这个目录"
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

    // 显示删除操作结果的 Composable 函数
    @Composable
    private fun DeleteDirectoryMessage(directoryPath: String, deletionResult: MutableState<String?>) {
        // 根据 deletionResult 的值显示不同的文本内容
        Text(
            text = deletionResult.value ?: "操作结果未知",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AutoDeleteDirTheme {
        Text("Preview")
    }
}

