package uz.javokhirdev.svocabulary.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Vocab in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object VocabIcons {
    val Add = Icons.Rounded.Add
    val ArrowBack = Icons.Rounded.ArrowBack
    val Clear = Icons.Rounded.ClearAll
    val Close = Icons.Rounded.Close
    val Copy = Icons.Rounded.ContentCopy
    val DarkMode = Icons.Rounded.DarkMode
    val Delete = Icons.Rounded.Delete
    val Edit = Icons.Rounded.Edit
    val Export = Icons.Rounded.FileDownload
    val FitnessCenter = Icons.Rounded.FitnessCenter
    val Info = Icons.Rounded.Info
    val Import = Icons.Rounded.FileUpload
    val LightMode = Icons.Rounded.LightMode
    val Mail = Icons.Rounded.Mail
    val Refresh = Icons.Rounded.Refresh
    val Save = Icons.Rounded.Save
    val Search = Icons.Rounded.Search
    val Settings = Icons.Rounded.Settings
    val Share = Icons.Rounded.Share
    val ThumbUp = Icons.Rounded.ThumbUp
    val ThumbDown = Icons.Rounded.ThumbDown
    val VolumeUp = Icons.Rounded.VolumeUp
    val Code = Icons.Rounded.Code
}

/**
 * A sealed class to make dealing with [ImageVector] and [DrawableRes] icons easier.
 */
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
