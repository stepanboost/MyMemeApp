package com.example.mymemeapp


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymemeapp.data.remote.ApiInterface
import com.example.mymemeapp.models.Meme
import com.example.mymemeapp.screens.DetailsScreen
import com.example.mymemeapp.screens.HomeScreen
import com.example.mymemeapp.ui.theme.MyMemeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
           var responce = apiInterface.getAllMemes()
        }


        setContent {
            MyMemeAppTheme {


                val navController = rememberNavController()
                var memesList by remember {
                    mutableStateOf(listOf<Meme>())
                }
                val scope = rememberCoroutineScope()

                LaunchedEffect(key1 = true) {
                    scope.launch(Dispatchers.IO) {
                        val response = try {
                            apiInterface.getAllMemes()
                        } catch (e: IOException) {
                            Toast.makeText(
                                this@MainActivity,
                                "APP ERROR: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        } catch (e: HttpException) {
                            Toast.makeText(
                                this@MainActivity,
                                "HTTP ERROR: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@launch
                        }
                        if (response.isSuccessful && response.body() != null) {
                            withContext(Dispatchers.Main) {
                                memesList = response.body()!!.data.memes
                            }
                        }
                    }
                }

                NavHost(navController = navController, startDestination = "HomeScreen") {

                    composable(route = "HomeScreen") {
                        HomeScreen(memesList = memesList, navController = navController)
                    }
                    composable(route = "DetailsScreen?name={name}&url={url}",
                        arguments = listOf(
                            navArgument(name = "name") {
                                type = NavType.StringType
                            },
                            navArgument(name = "url") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        DetailsScreen(
                            name = it.arguments?.getString("name"),
                            url = it.arguments?.getString("url")
                        )
                    }

                }
            }
        }
    }
}