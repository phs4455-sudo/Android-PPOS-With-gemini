package com.hd.hdmobilepos.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hd.hdmobilepos.FoodCourtViewModel
import com.hd.hdmobilepos.PaymentViewModel
import com.hd.hdmobilepos.RestaurantViewModel
import com.hd.hdmobilepos.SuperHomeViewModel
import com.hd.hdmobilepos.ui.screen.FoodCourtScreen
import com.hd.hdmobilepos.ui.screen.PaymentScreen
import com.hd.hdmobilepos.ui.screen.ProductRegisterScreen
import com.hd.hdmobilepos.ui.screen.RestaurantScreen
import com.hd.hdmobilepos.ui.screen.SuperHomeScreen

private const val ROUTE_RESTAURANT = "restaurant"
private const val ROUTE_SUPER_HOME = "super_home"
private const val ROUTE_PRODUCT_REGISTER = "product_register"
private const val ROUTE_PRODUCT_REGISTER_WITH_ARG = "product_register?barcode={barcode}"
private const val ARG_BARCODE = "barcode"

private fun productRegisterRoute(barcode: String? = null): String {
    return if (barcode.isNullOrBlank()) ROUTE_PRODUCT_REGISTER else "$ROUTE_PRODUCT_REGISTER?$ARG_BARCODE=${Uri.encode(barcode)}"
}

@Composable
fun MainNavHost(restaurantVm: RestaurantViewModel, foodCourtVm: FoodCourtViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ROUTE_SUPER_HOME) {
        composable(ROUTE_SUPER_HOME) {
            val superHomeVm: SuperHomeViewModel = viewModel()
            SuperHomeScreen(
                superHomeVm = superHomeVm,
                onNavigateToProductRegister = { barcode ->
                    navController.navigate(productRegisterRoute(barcode))
                },
                onNavigateToRestaurant = {
                    navController.navigate(ROUTE_RESTAURANT)
                }
            )
        }
        composable(ROUTE_RESTAURANT) { RestaurantScreen(navController, restaurantVm) }
        composable("food/{tableId}") { backStackEntry ->
            val tableId = backStackEntry.arguments?.getString("tableId")?.toLongOrNull()
            FoodCourtScreen(navController = navController, vm = foodCourtVm, tableId = tableId)
        }
        composable(ROUTE_PRODUCT_REGISTER_WITH_ARG) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString(ARG_BARCODE)
            ProductRegisterScreen(
                barcode = barcode,
                onNavigateHome = {
                    navController.navigate(ROUTE_SUPER_HOME) {
                        popUpTo(ROUTE_SUPER_HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("payment/{tableId}") { backStackEntry ->
            val tableId = backStackEntry.arguments?.getString("tableId")?.toLongOrNull()
            val snapshot = remember(tableId, foodCourtVm.uiState.collectAsState().value.rightPanel) { foodCourtVm.buildPaymentSnapshot(tableId) }
            val paymentVm: PaymentViewModel = viewModel(
                key = "payment_${tableId ?: -1}",
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T = PaymentViewModel(snapshot) as T
                }
            )
            PaymentScreen(navController = navController, paymentVm = paymentVm)
        }
    }
}
