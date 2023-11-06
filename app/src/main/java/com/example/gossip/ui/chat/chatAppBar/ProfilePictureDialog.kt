package com.example.gossip.ui.chat.chatAppBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.gossip.R

@Composable
fun ProfilePictureDialog(
    profilePictureUrl: Any,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Card() {
            Image(
                painter = rememberAsyncImagePainter(profilePictureUrl),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(260.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilePictureDialogPreview() {
    ProfilePictureDialog(
        profilePictureUrl = R.drawable.baseline_account_circle_24,
        onDismiss = {}
    )
}