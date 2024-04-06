import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.stericson.RootTools.RootTools
import java.io.File

class MainActivity : ComponentActivity() {

    private val directoryPath = "/data/local/cfg-znz"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val deletionResult = deleteDirectory(directoryPath)
                    DeleteDirectoryMessage(deletionResult)
                }
            }
        }
    }

    private fun deleteDirectory(directoryPath: String): String {

        return if (RootTools.isRootAvailable() && RootTools.isAccessGiven()) {
            val directory = File(directoryPath)
            if (directory.exists() && directory.isDirectory) {
                val deleted = RootTools.deleteFileOrDirectory(directoryPath, true)
                if (deleted) {
                    "删除成功"
                } else {
                    "删除失败"
                }
            } else {
                "没有发现这个目录"
            }
        } else {
            "无法获取 Root 权限"
        }
    }

    @Composable
    private fun DeleteDirectoryMessage(deletionResult: String) {
        Text(
            text = deletionResult,
            modifier = Modifier.fillMaxSize()
        )
    }
}
