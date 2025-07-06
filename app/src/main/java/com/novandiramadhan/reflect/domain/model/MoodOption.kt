package com.novandiramadhan.reflect.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class MoodOption(
    val level: Int,
    val icon: ImageVector,
    val label: String,
    val color: Color
)