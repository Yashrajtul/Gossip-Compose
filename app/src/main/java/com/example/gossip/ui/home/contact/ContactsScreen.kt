package com.example.gossip.ui.home.contact

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.gossip.R
import com.example.gossip.model.Contact

@Composable
fun ContactsScreen(contacts: List<Contact>) {
    LazyColumn{
        items(contacts){contact->
            ContactUnit(contact = contact)
        }
    }
}

@Composable
fun ContactUnit(contact: Contact) {
    Row {
        AsyncImage(model = R.drawable.baseline_account_circle_24, contentDescription = "")
        Column {
            Text(text = contact.name)
            Text(text = contact.phoneNumber)
        }
    }
}

@Preview
@Composable
fun ContactUnitPreview() {
    ContactUnit(contact = Contact(name = "Yash", phoneNumber = "+919876543210"))
}