package com.slmoney.app.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

object SLMoneyMotion {
    // Standard enter/exit springs (M3 Expressive style)
    val SpringEnter = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,   // 0.5 — bouncy
        stiffness = Spring.StiffnessMedium              // 1500
    )
    val SpringExit = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh
    )
    // FAB expansion spring
    val SpringFab = spring<Float>(
        dampingRatio = 0.6f,
        stiffness = 800f
    )
    // List item stagger delay
    const val StaggerDelayMs = 50
}
