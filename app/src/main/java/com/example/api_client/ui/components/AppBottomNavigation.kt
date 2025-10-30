import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.data.Group
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. Definición de las Rutas de Navegación ---
// Usar una sealed class hace que sea seguro y fácil de mantener
sealed class NavRoute(
    val route: String,
    val label: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : NavRoute(
        route = "inicio",
        label = "Inicio",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )

    object Clients : NavRoute(
        route = "clientes",
        label = "Clientes",
        unselectedIcon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person
    )

    object OTs : NavRoute(
        route = "ots",
        label = "OT's",
        unselectedIcon = Icons.Outlined.MailOutline,
        selectedIcon = Icons.Filled.MailOutline
    )
}

// Lista de items para la barra de navegación
val bottomNavItems = listOf(
    NavRoute.Home,
    NavRoute.Clients,
    NavRoute.OTs
)

// --- 2. El Componente Contenedor de la Barra de Navegación ---
@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFFF8F9FF)
    ) {
        // Usamos un Row para alinear los items como en la imagen (distribuidos)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp) // Altura fija
                .padding(vertical = 15.dp), // Padding vertical del contenedor
            horizontalArrangement = Arrangement.SpaceAround, // Distribuye los items
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                CustomNavItem(
                    label = item.label,
                    unselectedIcon = item.unselectedIcon,
                    selectedIcon = item.selectedIcon,
                    isSelected = item.route == currentRoute,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

// --- 3. El Componente Individual de Cada Botón ---
@Composable
fun CustomNavItem(
    label: String,
    unselectedIcon: ImageVector,
    selectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp), // 5 de gap vertical
        modifier = Modifier.clickable { onClick() }
    ) {
        // --- Lógica del Icono Activo/Inactivo ---
        Box(contentAlignment = Alignment.Center) {
            if (isSelected) {
                // Estado Activo
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = Color(0xFF366A9A)
                ) {
                    Icon(
                        imageVector = selectedIcon,
                        contentDescription = label,
                        tint = Color.White,
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
                    )
                }
            } else {
                // Estado Inactivo
                Icon(
                    imageVector = unselectedIcon,
                    contentDescription = label,
                    tint = Color(0xFF5D5F5F)
                )
            }
        }

        // --- Texto ---
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF5D5F5F)
        )
    }
}


// --- 4. Vista Previa para Pruebas ---
@Preview(showBackground = true)
@Composable
fun AppBottomNavigationPreview() {
    // Simula el estado de la ruta actual
    var currentRoute by remember { mutableStateOf(NavRoute.Clients.route) }

    AppBottomNavigation(
        currentRoute = currentRoute,
        onNavigate = { newRoute ->
            currentRoute = newRoute
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppBottomNavigationPreview_Home() {
    var currentRoute by remember { mutableStateOf(NavRoute.Home.route) }

    AppBottomNavigation(
        currentRoute = currentRoute,
        onNavigate = { newRoute ->
            currentRoute = newRoute
        }
    )
}
