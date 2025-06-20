package com.jry.tareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    var showMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var darkMode by remember { mutableStateOf(false) }

    val colorScheme = if (darkMode) {
        darkColorScheme(
            primary = Color(0xFF228B22),
            onPrimary = Color.White,
            secondary = Color(0xFF228B22),
            onSecondary = Color.White,
            surface = Color(0xFF181A20),
            onSurface = Color.White,
            background = Color(0xFF121212)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF66BB6A),  //Color(0xFFF5F5F5)
            onPrimary = Color.Black,
            secondary = Color(0xFF66BB6A),
            onSecondary = Color.White,
            surface = Color.White,
            onSurface = Color.Black,
            background = Color(0xFFF2F2F2)
        )
    }

    MaterialTheme(colorScheme = colorScheme) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()) {

                // Barra arriba (FALTA EL BOTON DE BUSQUEDA)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "Tasks",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                        )

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            IconButton(onClick = { /* TODO: Acción de búsqueda */ }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar",
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }

                // Lista de tareas (BASE SIN DESARROLLO)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // 80%
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(50) { index ->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .fillMaxWidth()
                                .height(80.dp),

                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Ítem $index",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Barra abajo (BASE SIN DESARROLLO)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* TODO: Acción de filtro */ }) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = "Filtros",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            IconButton(onClick = { /* TODO: Acción de agregar */ }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar",
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }

            if (showMenu) {
                ModalBottomSheet(
                    onDismissRequest = { showMenu = false },
                    sheetState = sheetState
                ) {
                    MenuUI(
                        onClose = { showMenu = false },
                        darkMode = darkMode,
                        onDarkModeChange = { darkMode = it }
                    )
                }
            }
        }
    }
}