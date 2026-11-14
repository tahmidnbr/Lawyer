package com.example.lawyer.presentation.extra

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lawyer.ui.theme.PrimaryBlack

@Composable
fun CustomTextField(
    value : String,
    onValueChange: (String) -> Unit,
    modifier : Modifier = Modifier,
    placeholder: String
){
    val customText = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Normal
    )
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = customText,
        singleLine = true,
        modifier = modifier,

        decorationBox = { innerFiled ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, PrimaryBlack.copy(.05f), RoundedCornerShape(8.dp))
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ){
                        if (value.isEmpty()){
                            Text(
                                placeholder,
                                style = customText,
                                color = PrimaryBlack.copy(.5f)
                            )
                        }

                        innerFiled()
                    }

                    if (value.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear, // Changed to Clear since it only shows when text is present
                            contentDescription = "Clear text",
                            tint = PrimaryBlack.copy(.5f),
                            modifier = Modifier
                                .clickable(onClick = { onValueChange("")})
                                .size(15.dp)
                        )

                    }
                }
            }
        }
    )
}

object CourtOptions {
    val courts = listOf(
        "Supreme Court of Bangladesh",
        "High Court Division",
        "Appellate Division",
        // Add the remaining court names here
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDropdown(
    selectedCourt: String,
    onCourtSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        BasicTextField(
            value = selectedCourt,
            onValueChange = {},
            readOnly = true,
            decorationBox = { innerFiled ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, PrimaryBlack.copy(.05f), RoundedCornerShape(8.dp))
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ){
                            if (selectedCourt.isEmpty()){
                                Text(
                                    "Court Name",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Normal
                                    ),
                                    color = PrimaryBlack.copy(.5f)
                                )
                            }

                            innerFiled()
                        }

                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CourtOptions.courts.forEach { court ->
                DropdownMenuItem(
                    text = { Text(court) },
                    onClick = {
                        onCourtSelected(court)
                        expanded = false
                    }
                )
            }
        }
    }
}