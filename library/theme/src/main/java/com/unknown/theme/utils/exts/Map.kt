package com.unknown.theme.utils.exts


fun Map<String, Any>.colorOrTransparent(key: String): Int =
    this[key] as? Int ?: android.graphics.Color.TRANSPARENT

val Map<String, Any>.colorPrimary: Int get() = colorOrTransparent("colorPrimary") // Màu chính của ứng dụng
val Map<String, Any>.colorOnPrimary: Int get() = colorOrTransparent("colorOnPrimary") // Màu nội dung trên màu chính
val Map<String, Any>.colorPrimaryContainer: Int get() = colorOrTransparent("colorPrimaryContainer") // Nền phụ của màu chính
val Map<String, Any>.colorOnPrimaryContainer: Int get() = colorOrTransparent("colorOnPrimaryContainer") // Màu nội dung trên nền phụ của màu chính

val Map<String, Any>.colorSecondary: Int get() = colorOrTransparent("colorSecondary") // Màu phụ (thứ hai)
val Map<String, Any>.colorOnSecondary: Int get() = colorOrTransparent("colorOnSecondary") // Màu nội dung trên màu phụ
val Map<String, Any>.colorSecondaryContainer: Int get() = colorOrTransparent("colorSecondaryContainer") // Nền phụ của màu phụ
val Map<String, Any>.colorOnSecondaryContainer: Int get() = colorOrTransparent("colorOnSecondaryContainer") // Màu nội dung trên nền phụ của màu phụ

val Map<String, Any>.colorTertiary: Int get() = colorOrTransparent("colorTertiary") // Màu hỗ trợ (thứ ba)
val Map<String, Any>.colorOnTertiary: Int get() = colorOrTransparent("colorOnTertiary") // Màu nội dung trên màu hỗ trợ
val Map<String, Any>.colorTertiaryContainer: Int get() = colorOrTransparent("colorTertiaryContainer") // Nền phụ của màu hỗ trợ
val Map<String, Any>.colorOnTertiaryContainer: Int get() = colorOrTransparent("colorOnTertiaryContainer") // Màu nội dung trên nền phụ của màu hỗ trợ

val Map<String, Any>.colorError: Int get() = colorOrTransparent("colorError") // Màu lỗi
val Map<String, Any>.colorOnError: Int get() = colorOrTransparent("colorOnError") // Màu nội dung trên nền lỗi
val Map<String, Any>.colorErrorContainer: Int get() = colorOrTransparent("colorErrorContainer") // Nền phụ của màu lỗi
val Map<String, Any>.colorOnErrorContainer: Int get() = colorOrTransparent("colorOnErrorContainer") // Màu nội dung trên nền phụ của lỗi

val Map<String, Any>.colorBackground: Int get() = colorOrTransparent("colorBackground") // Màu nền tổng thể của app
val Map<String, Any>.colorOnBackground: Int get() = colorOrTransparent("colorOnBackground") // Màu nội dung trên nền chính
val Map<String, Any>.colorSurface: Int get() = colorOrTransparent("colorSurface") // Màu nền bề mặt chính (cards, sheets, menus, v.v.)
val Map<String, Any>.colorOnSurface: Int get() = colorOrTransparent("colorOnSurface") // Màu nội dung trên bề mặt
val Map<String, Any>.colorSurfaceVariant: Int get() = colorOrTransparent("colorSurfaceVariant") // Biến thể của màu nền
val Map<String, Any>.colorOnSurfaceVariant: Int get() = colorOrTransparent("colorOnSurfaceVariant") // Màu nội dung trên nền biến thể

val Map<String, Any>.colorOutline: Int get() = colorOrTransparent("colorOutline") // Màu viền
val Map<String, Any>.colorOutlineVariant: Int get() = colorOrTransparent("colorOutlineVariant") // Biến thể của màu viền
val Map<String, Any>.colorSurfaceTint: Int get() = colorOrTransparent("colorSurfaceTint") // Hiệu ứng tint của bề mặt

val Map<String, Any>.colorInverseSurface: Int get() = colorOrTransparent("colorInverseSurface") // Nền đảo ngược (cho dark/light switch)
val Map<String, Any>.colorInverseOnSurface: Int get() = colorOrTransparent("colorInverseOnSurface") // Màu nội dung trên nền đảo ngược
val Map<String, Any>.colorInversePrimary: Int get() = colorOrTransparent("colorInversePrimary") // Màu chính đảo ngược

val Map<String, Any>.colorSurfaceDim: Int get() = colorOrTransparent("colorSurfaceDim") // Nền dim nhẹ
val Map<String, Any>.colorSurfaceBright: Int get() = colorOrTransparent("colorSurfaceBright") // Nền sáng
val Map<String, Any>.colorSurfaceContainerLowest: Int get() = colorOrTransparent("colorSurfaceContainerLowest") // Nền mức thấp nhất
val Map<String, Any>.colorSurfaceContainerLow: Int get() = colorOrTransparent("colorSurfaceContainerLow") // Nền mức thấp
val Map<String, Any>.colorSurfaceContainer: Int get() = colorOrTransparent("colorSurfaceContainer") // Nền trung bình
val Map<String, Any>.colorSurfaceContainerHigh: Int get() = colorOrTransparent("colorSurfaceContainerHigh") // Nền mức cao
val Map<String, Any>.colorSurfaceContainerHighest: Int get() = colorOrTransparent("colorSurfaceContainerHighest") // Nền mức cao nhất

val Map<String, Any>.colorControlActivated: Int get() = colorOrTransparent("colorControlActivated") // Màu khi điều khiển được kích hoạt
val Map<String, Any>.colorControlHighlight: Int get() = colorOrTransparent("colorControlHighlight") // Màu hiệu ứng nhấn
val Map<String, Any>.colorControlNormal: Int get() = colorOrTransparent("colorControlNormal") // Màu điều khiển mặc định

val Map<String, Any>.colorPrimaryFixed: Int get() = colorOrTransparent("colorPrimaryFixed") // Màu cố định primary (dynamic tone)
val Map<String, Any>.colorPrimaryFixedDim: Int get() = colorOrTransparent("colorPrimaryFixedDim") // Biến thể dim của primary fixed
val Map<String, Any>.colorOnPrimaryFixed: Int get() = colorOrTransparent("colorOnPrimaryFixed") // Màu nội dung trên primary fixed
val Map<String, Any>.colorOnPrimaryFixedVariant: Int get() = colorOrTransparent("colorOnPrimaryFixedVariant") // Biến thể nội dung primary fixed

val Map<String, Any>.colorSecondaryFixed: Int get() = colorOrTransparent("colorSecondaryFixed") // Màu cố định secondary
val Map<String, Any>.colorSecondaryFixedDim: Int get() = colorOrTransparent("colorSecondaryFixedDim") // Biến thể dim của secondary fixed
val Map<String, Any>.colorOnSecondaryFixed: Int get() = colorOrTransparent("colorOnSecondaryFixed") // Màu nội dung trên secondary fixed
val Map<String, Any>.colorOnSecondaryFixedVariant: Int get() = colorOrTransparent("colorOnSecondaryFixedVariant") // Biến thể nội dung secondary fixed

val Map<String, Any>.colorTertiaryFixed: Int get() = colorOrTransparent("colorTertiaryFixed") // Màu cố định tertiary
val Map<String, Any>.colorTertiaryFixedDim: Int get() = colorOrTransparent("colorTertiaryFixedDim") // Biến thể dim của tertiary fixed
val Map<String, Any>.colorOnTertiaryFixed: Int get() = colorOrTransparent("colorOnTertiaryFixed") // Màu nội dung trên tertiary fixed
val Map<String, Any>.colorOnTertiaryFixedVariant: Int get() = colorOrTransparent("colorOnTertiaryFixedVariant") // Biến thể nội dung tertiary fixed

val Map<String, Any>.colorOnErrorFixed: Int get() = colorOrTransparent("colorOnErrorFixed") // Màu nội dung trên màu lỗi cố định
val Map<String, Any>.colorOnErrorFixedVariant: Int get() = colorOrTransparent("colorOnErrorFixedVariant") // Biến thể nội dung lỗi cố định