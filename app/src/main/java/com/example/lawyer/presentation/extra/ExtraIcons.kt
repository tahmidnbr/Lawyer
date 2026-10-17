package com.example.lawyer.presentation.extra

/*
MIT License

Copyright (c) 2020 Microsoft Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// --- 1. FILLED VERSION ---

val FluentuiSystemIconsHome: ImageVector
    get() {
        if (_FluentuiSystemIconsHome != null) return _FluentuiSystemIconsHome!!

        _FluentuiSystemIconsHome = ImageVector.Builder(
            name = "home",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(13.4508f, 2.53318f)
                curveTo(12.6128f, 1.82618f, 11.3872f, 1.82618f, 10.5492f, 2.53318f)
                lineTo(3.79916f, 8.22772f)
                curveTo(3.29241f, 8.65523f, 3f, 9.28447f, 3f, 9.94747f)
                verticalLineTo(19.2526f)
                curveTo(3f, 20.2191f, 3.7835f, 21.0026f, 4.75f, 21.0026f)
                horizontalLineTo(7.75f)
                curveTo(8.7165f, 21.0026f, 9.5f, 20.2191f, 9.5f, 19.2526f)
                verticalLineTo(15.25f)
                curveTo(9.5f, 14.5707f, 10.0418f, 14.018f, 10.7169f, 14.0004f)
                horizontalLineTo(13.2831f)
                curveTo(13.9582f, 14.018f, 14.5f, 14.5707f, 14.5f, 15.25f)
                verticalLineTo(19.2526f)
                curveTo(14.5f, 20.2191f, 15.2835f, 21.0026f, 16.25f, 21.0026f)
                horizontalLineTo(19.25f)
                curveTo(20.2165f, 21.0026f, 21f, 20.2191f, 21f, 19.2526f)
                verticalLineTo(9.94747f)
                curveTo(21f, 9.28447f, 20.7076f, 8.65523f, 20.2008f, 8.22772f)
                lineTo(13.4508f, 2.53318f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsHome!!
    }

private var _FluentuiSystemIconsHome: ImageVector? = null


// --- 2. REGULAR / EMPTY VERSION ---
val HomeRegular: ImageVector
    get() {
        if (_HomeRegular != null) return _HomeRegular!!

        _HomeRegular = ImageVector.Builder(
            name = "home-regular", // Unique internal name
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(10.5491f, 2.53261f)
                curveTo(11.3872f, 1.8256f, 12.6128f, 1.82559f, 13.4509f, 2.53261f)
                lineTo(20.2009f, 8.22729f)
                curveTo(20.7076f, 8.65481f, 21f, 9.28404f, 21f, 9.94703f)
                verticalLineTo(18.75f)
                curveTo(21f, 19.9926f, 19.9926f, 21f, 18.75f, 21f)
                horizontalLineTo(5.25f)
                curveTo(4.00736f, 21f, 3f, 19.9926f, 3f, 18.75f)
                verticalLineTo(9.94703f)
                curveTo(3f, 9.28404f, 3.2924f, 8.65481f, 3.79913f, 8.22729f)
                lineTo(10.5491f, 2.53261f)
                close()
                moveTo(12.4836f, 3.6791f)
                curveTo(12.2043f, 3.44343f, 11.7957f, 3.44343f, 11.5164f, 3.67911f)
                lineTo(4.76638f, 9.37379f)
                curveTo(4.59747f, 9.51629f, 4.5f, 9.72603f, 4.5f, 9.94703f)
                verticalLineTo(18.75f)
                curveTo(4.5f, 19.1642f, 4.83579f, 19.5f, 5.25f, 19.5f)
                horizontalLineTo(18.75f)
                curveTo(19.1642f, 19.5f, 19.5f, 19.1642f, 19.5f, 18.75f)
                verticalLineTo(9.94703f)
                curveTo(19.5f, 9.72603f, 19.4025f, 9.51629f, 19.2336f, 9.37378f)
                lineTo(12.4836f, 3.6791f)
                close()
            }
        }.build()

        return _HomeRegular!!
    }

private var _HomeRegular: ImageVector? = null

//3

val FluentuiSystemIconsFolder: ImageVector
    get() {
        if (_FluentuiSystemIconsFolder != null) return _FluentuiSystemIconsFolder!!

        _FluentuiSystemIconsFolder = ImageVector.Builder(
            name = "folder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(3.5f, 6.25f)
                verticalLineTo(8f)
                horizontalLineTo(8.12868f)
                curveTo(8.32759f, 8f, 8.51836f, 7.92098f, 8.65901f, 7.78033f)
                lineTo(10.1893f, 6.25f)
                lineTo(8.65901f, 4.71967f)
                curveTo(8.51836f, 4.57902f, 8.32759f, 4.5f, 8.12868f, 4.5f)
                horizontalLineTo(5.25f)
                curveTo(4.2835f, 4.5f, 3.5f, 5.2835f, 3.5f, 6.25f)
                close()
                moveTo(2f, 6.25f)
                curveTo(2f, 4.45507f, 3.45507f, 3f, 5.25f, 3f)
                horizontalLineTo(8.12868f)
                curveTo(8.72542f, 3f, 9.29771f, 3.23705f, 9.71967f, 3.65901f)
                lineTo(11.5607f, 5.5f)
                horizontalLineTo(18.75f)
                curveTo(20.5449f, 5.5f, 22f, 6.95507f, 22f, 8.75f)
                verticalLineTo(17.75f)
                curveTo(22f, 19.5449f, 20.5449f, 21f, 18.75f, 21f)
                horizontalLineTo(5.25f)
                curveTo(3.45507f, 21f, 2f, 19.5449f, 2f, 17.75f)
                verticalLineTo(6.25f)
                close()
                moveTo(3.5f, 9.5f)
                verticalLineTo(17.75f)
                curveTo(3.5f, 18.7165f, 4.2835f, 19.5f, 5.25f, 19.5f)
                horizontalLineTo(18.75f)
                curveTo(19.7165f, 19.5f, 20.5f, 18.7165f, 20.5f, 17.75f)
                verticalLineTo(8.75f)
                curveTo(20.5f, 7.7835f, 19.7165f, 7f, 18.75f, 7f)
                horizontalLineTo(11.5607f)
                lineTo(9.71967f, 8.84099f)
                curveTo(9.29771f, 9.26295f, 8.72542f, 9.5f, 8.12868f, 9.5f)
                horizontalLineTo(3.5f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsFolder!!
    }

private var _FluentuiSystemIconsFolder: ImageVector? = null

//4

val FluentuiSystemIconsFolderOpen: ImageVector
    get() {
        if (_FluentuiSystemIconsFolderOpen != null) return _FluentuiSystemIconsFolderOpen!!

        _FluentuiSystemIconsFolderOpen = ImageVector.Builder(
            name = "folder-open",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(5.25f, 3f)
                curveTo(3.45507f, 3f, 2f, 4.45507f, 2f, 6.25f)
                verticalLineTo(15.1618f)
                lineTo(4.04451f, 11.6206f)
                curveTo(4.8037f, 10.3057f, 6.20674f, 9.49561f, 7.72512f, 9.49561f)
                lineTo(20f, 9.49561f)
                verticalLineTo(8.75f)
                curveTo(20f, 6.95507f, 18.5449f, 5.5f, 16.75f, 5.5f)
                horizontalLineTo(11.5607f)
                lineTo(9.71967f, 3.65901f)
                curveTo(9.29771f, 3.23705f, 8.72542f, 3f, 8.12868f, 3f)
                horizontalLineTo(5.25f)
                close()
                moveTo(7.72512f, 10.9956f)
                lineTo(20.7445f, 10.9956f)
                curveTo(22.4765f, 10.9956f, 23.5591f, 12.8706f, 22.6931f, 14.3706f)
                lineTo(19.6569f, 19.6294f)
                curveTo(19.1657f, 20.4802f, 18.2578f, 21.0044f, 17.2753f, 21.0044f)
                horizontalLineTo(4.25594f)
                curveTo(2.52389f, 21.0044f, 1.44136f, 19.1294f, 2.30738f, 17.6294f)
                lineTo(5.34355f, 12.3706f)
                curveTo(5.83479f, 11.5198f, 6.74264f, 10.9956f, 7.72512f, 10.9956f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsFolderOpen!!
    }

private var _FluentuiSystemIconsFolderOpen: ImageVector? = null

//5
val CalendarEmptyFilled: ImageVector
    get() {
        if (_CalendarEmptyFilled != null) return _CalendarEmptyFilled!!

        _CalendarEmptyFilled = ImageVector.Builder(
            name = "calendar-empty",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(21f, 8.5f)
                verticalLineTo(17.75f)
                curveTo(21f, 19.5449f, 19.5449f, 21f, 17.75f, 21f)
                horizontalLineTo(6.25f)
                curveTo(4.45507f, 21f, 3f, 19.5449f, 3f, 17.75f)
                verticalLineTo(8.5f)
                horizontalLineTo(21f)
                close()
                moveTo(17.75f, 3f)
                curveTo(19.5449f, 3f, 21f, 4.45507f, 21f, 6.25f)
                verticalLineTo(7f)
                horizontalLineTo(3f)
                verticalLineTo(6.25f)
                curveTo(3f, 4.45507f, 4.45507f, 3f, 6.25f, 3f)
                horizontalLineTo(17.75f)
                close()
            }
        }.build()

        return _CalendarEmptyFilled!!
    }

private var _CalendarEmptyFilled: ImageVector? = null

//6

val CalendarEmpty: ImageVector
    get() {
        if (_CalendarEmpty != null) return _CalendarEmpty!!

        _CalendarEmpty = ImageVector.Builder(
            name = "calendar-empty",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(17.75f, 3f)
                curveTo(19.5449f, 3f, 21f, 4.45507f, 21f, 6.25f)
                verticalLineTo(17.75f)
                curveTo(21f, 19.5449f, 19.5449f, 21f, 17.75f, 21f)
                horizontalLineTo(6.25f)
                curveTo(4.45507f, 21f, 3f, 19.5449f, 3f, 17.75f)
                verticalLineTo(6.25f)
                curveTo(3f, 4.45507f, 4.45507f, 3f, 6.25f, 3f)
                horizontalLineTo(17.75f)
                close()
                moveTo(19.5f, 8.5f)
                horizontalLineTo(4.5f)
                verticalLineTo(17.75f)
                curveTo(4.5f, 18.7165f, 5.2835f, 19.5f, 6.25f, 19.5f)
                horizontalLineTo(17.75f)
                curveTo(18.7165f, 19.5f, 19.5f, 18.7165f, 19.5f, 17.75f)
                verticalLineTo(8.5f)
                close()
                moveTo(17.75f, 4.5f)
                horizontalLineTo(6.25f)
                curveTo(5.2835f, 4.5f, 4.5f, 5.2835f, 4.5f, 6.25f)
                verticalLineTo(7f)
                horizontalLineTo(19.5f)
                verticalLineTo(6.25f)
                curveTo(19.5f, 5.2835f, 18.7165f, 4.5f, 17.75f, 4.5f)
                close()
            }
        }.build()

        return _CalendarEmpty!!
    }

private var _CalendarEmpty: ImageVector? = null


val LucideScale: ImageVector
    get() {
        if (_LucideScale != null) return _LucideScale!!

        _LucideScale = ImageVector.Builder(
            name = "scale",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 3f)
                verticalLineToRelative(18f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(19f, 8f)
                lineToRelative(3f, 8f)
                arcToRelative(5f, 5f, 0f, false, true, -6f, 0f)
                close()
                verticalLineTo(7f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 7f)
                horizontalLineToRelative(1f)
                arcToRelative(17f, 17f, 0f, false, false, 8f, -2f)
                arcToRelative(17f, 17f, 0f, false, false, 8f, 2f)
                horizontalLineToRelative(1f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 8f)
                lineToRelative(3f, 8f)
                arcToRelative(5f, 5f, 0f, false, true, -6f, 0f)
                close()
                verticalLineTo(7f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7f, 21f)
                horizontalLineToRelative(10f)
            }
        }.build()

        return _LucideScale!!
    }

private var _LucideScale: ImageVector? = null