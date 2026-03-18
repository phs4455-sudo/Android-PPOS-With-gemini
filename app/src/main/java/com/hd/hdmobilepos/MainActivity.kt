package com.hd.hdmobilepos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.hd.hdmobilepos.data.AppDatabase
import com.hd.hdmobilepos.data.PosRepository
import com.hd.hdmobilepos.navigation.MainNavHost
import com.hd.hdmobilepos.ui.theme.PPOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "ppos.db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
        val repo = PosRepository(db.posDao())

        setContent {
            PPOSTheme {
                val restaurantVm: RestaurantViewModel = viewModel(
                    key = "restaurant_vm",
                    factory = remember {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                                RestaurantViewModel(repo) as T
                        }
                    }
                )
                val foodCourtVm: FoodCourtViewModel = viewModel(
                    key = "food_court_vm",
                    factory = remember {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                                FoodCourtViewModel(repo) as T
                        }
                    }
                )
                MainNavHost(restaurantVm = restaurantVm, foodCourtVm = foodCourtVm)
            }
        }
    }
}
