package com.jry.tareas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

enum class SortOrder {
    DEFAULT,
    ASCENDING,
    DESCENDING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val contexto = LocalContext.current
    val database = remember { TaskDatabase.getDatabase(contexto) }
    val taskDao = database.taskDao()
    val tareas by taskDao.getAllTasks().collectAsState(initial = emptyList())

    val estadoModal = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val datos = remember { Datos(contexto) }
    val modoOscuro by datos.darkMode.collectAsState(initial = false)

    var orden by remember { mutableStateOf(SortOrder.DEFAULT) }
    val tareasOrdenadas by remember(tareas, orden) {
        derivedStateOf {
            when (orden) {
                SortOrder.ASCENDING -> tareas.sortedBy { it.title }
                SortOrder.DESCENDING -> tareas.sortedByDescending { it.title }
                SortOrder.DEFAULT -> tareas
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Barra superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { scope.launch { estadoModal.show() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { navController.navigate("search") }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }

            // Lista de tareas
            if (tareas.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LottieAnimacion(assetName = "vacio.lottie", size = 250.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Aún no hay tareas",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "¡Añade una para empezar!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tareasOrdenadas, key = { it.id }) { tarea ->
                        Box(
                            modifier = Modifier
                                .shadow(2.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { navController.navigate("taskDetail/${tarea.id}") }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = tarea.title,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Barra inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    var filtroMenuExpandido by remember { mutableStateOf(false) }

                    Box {
                        IconButton(onClick = { filtroMenuExpandido = true }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filtros",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        MaterialTheme(
                            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
                        ) {
                            DropdownMenu(
                                expanded = filtroMenuExpandido,
                                onDismissRequest = { filtroMenuExpandido = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Por creación") },
                                    onClick = {
                                        orden = SortOrder.DEFAULT
                                        filtroMenuExpandido = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Alfabéticamente (A-Z)") },
                                    onClick = {
                                        orden = SortOrder.ASCENDING
                                        filtroMenuExpandido = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Alfabéticamente (Z-A)") },
                                    onClick = {
                                        orden = SortOrder.DESCENDING
                                        filtroMenuExpandido = false
                                    }
                                )
                            }
                        }
                    }

                    var menuAgregarExpandido by remember { mutableStateOf(false) }

                    Box {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = { menuAgregarExpandido = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar",
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                        MaterialTheme(
                            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
                        ) {
                            DropdownMenu(
                                expanded = menuAgregarExpandido,
                                onDismissRequest = { menuAgregarExpandido = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Nota") },
                                    onClick = {
                                        navController.navigate("addTask")
                                        menuAgregarExpandido = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Imagen") },
                                    onClick = {
                                        navController.navigate("addImage")
                                        menuAgregarExpandido = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Tabla") },
                                    onClick = {
                                        navController.navigate("addTable")
                                        menuAgregarExpandido = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (estadoModal.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        estadoModal.hide()
                    }
                },
                sheetState = estadoModal
            ) {
                MenuUI(
                    modoOscuro = modoOscuro,
                    onModoOscuroChange = { nuevoValor ->
                        scope.launch {
                            datos.guardarModoOscuro(nuevoValor)
                        }
                    }
                )
            }
        }
    }
}
