package com.example.mobile_project_notes_app.components.Category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    isSheetOpen: Boolean,
    onSheetStateChange: (Boolean) -> Unit,
    onAddCategory: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var text by rememberSaveable { mutableStateOf("") }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                onSheetStateChange(false)
                text = ""
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "New Category",
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { newText -> text = newText },
                    placeholder = { Text(text = "Example: Work, TO-DO, Groceries") },
                    label = { Text(text = "Category") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(vertical = 100.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (text.isNotBlank()) {
                            onAddCategory(text)
                            text = ""
                        }
                    },
                    enabled = text.isNotBlank()
                ) {
                    Text("Add Category")
                }
            }
        }
    }
}