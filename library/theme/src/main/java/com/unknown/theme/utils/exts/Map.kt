package com.unknown.theme.utils.exts


fun Map<String, Any>.colorOrTransparent(key: String): Int =
    this[key] as? Int ?: android.graphics.Color.TRANSPARENT

// Primary colors
val Map<String, Any>.colorPrimary: Int get() = colorOrTransparent("colorPrimary")
val Map<String, Any>.colorOnPrimary: Int get() = colorOrTransparent("colorOnPrimary")
val Map<String, Any>.colorPrimaryContainer: Int get() = colorOrTransparent("colorPrimaryContainer")
val Map<String, Any>.colorOnPrimaryContainer: Int get() = colorOrTransparent("colorOnPrimaryContainer")

// Secondary colors
val Map<String, Any>.colorSecondary: Int get() = colorOrTransparent("colorSecondary")
val Map<String, Any>.colorOnSecondary: Int get() = colorOrTransparent("colorOnSecondary")
val Map<String, Any>.colorSecondaryContainer: Int get() = colorOrTransparent("colorSecondaryContainer")
val Map<String, Any>.colorOnSecondaryContainer: Int get() = colorOrTransparent("colorOnSecondaryContainer")

// Tertiary colors
val Map<String, Any>.colorTertiary: Int get() = colorOrTransparent("colorTertiary")
val Map<String, Any>.colorOnTertiary: Int get() = colorOrTransparent("colorOnTertiary")
val Map<String, Any>.colorTertiaryContainer: Int get() = colorOrTransparent("colorTertiaryContainer")
val Map<String, Any>.colorOnTertiaryContainer: Int get() = colorOrTransparent("colorOnTertiaryContainer")

// Error colors
val Map<String, Any>.colorError: Int get() = colorOrTransparent("colorError")
val Map<String, Any>.colorOnError: Int get() = colorOrTransparent("colorOnError")
val Map<String, Any>.colorErrorContainer: Int get() = colorOrTransparent("colorErrorContainer")
val Map<String, Any>.colorOnErrorContainer: Int get() = colorOrTransparent("colorOnErrorContainer")

// Background / Surface
val Map<String, Any>.colorBackground: Int get() = colorOrTransparent("colorBackground")
val Map<String, Any>.colorOnBackground: Int get() = colorOrTransparent("colorOnBackground")
val Map<String, Any>.colorSurface: Int get() = colorOrTransparent("colorSurface")
val Map<String, Any>.colorOnSurface: Int get() = colorOrTransparent("colorOnSurface")
val Map<String, Any>.colorSurfaceVariant: Int get() = colorOrTransparent("colorSurfaceVariant")
val Map<String, Any>.colorOnSurfaceVariant: Int get() = colorOrTransparent("colorOnSurfaceVariant")

// Other
val Map<String, Any>.colorOutline: Int get() = colorOrTransparent("colorOutline")
val Map<String, Any>.colorInverseOnSurface: Int get() = colorOrTransparent("colorInverseOnSurface")
val Map<String, Any>.colorInverseSurface: Int get() = colorOrTransparent("colorInverseSurface")
val Map<String, Any>.colorPrimaryInverse: Int get() = colorOrTransparent("colorPrimaryInverse")