package dev.wazapps.timetodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.wazapps.timetodo.navigation.SetupNavigation
import dev.wazapps.timetodo.ui.theme.TimeToDoTheme
import dev.wazapps.timetodo.ui.viewmodels.TaskSharedViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel : TaskSharedViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeToDoTheme {
                navController = rememberNavController()
                SetupNavigation(
                    navController = navController,
                    taskSharedViewModel = sharedViewModel
                )
            }
        }
    }
}