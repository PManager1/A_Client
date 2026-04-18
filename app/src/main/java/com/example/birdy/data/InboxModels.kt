package com.example.birdy.data

import java.util.UUID

// Matches iOS Inbox.swift data models

data class InboxMessage(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val timeAgo: String,
    val isUnread: Boolean = false
)

data class ScheduledRequest(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: String,
    val time: String
)

data class SupportTicket(
    val id: String = UUID.randomUUID().toString(),
    val ticketID: String,
    val status: String,
    val subject: String
)

object InboxData {

    val notifications = listOf(
        InboxMessage(title = "You received a \$40.00 tip!", timeAgo = "1 day ago", isUnread = true),
        InboxMessage(title = "You received a \$27.00 tip!", timeAgo = "1 day ago", isUnread = false),
        InboxMessage(title = "You received a \$100.00 tip!", timeAgo = "1 day ago", isUnread = false),
        InboxMessage(title = "You received a \$40.00 tip!", timeAgo = "1 day ago", isUnread = false),
        InboxMessage(title = "Your cashout of \$958.90 was successful", timeAgo = "1 day ago", isUnread = false),
        InboxMessage(title = "A New+ update: new earning", timeAgo = "2 days ago", isUnread = false)
    )

    val scheduledRequests = listOf(
        ScheduledRequest(title = "Babysitting for Sarah's kids", date = "Friday, Nov 22", time = "5:00 PM - 8:00 PM"),
        ScheduledRequest(title = "Dog walking for Buster", date = "Saturday, Nov 23", time = "10:00 AM - 11:00 AM"),
        ScheduledRequest(title = "Home cleaning service", date = "Monday, Nov 25", time = "1:00 PM - 3:00 PM")
    )

    val supportTickets = listOf(
        SupportTicket(ticketID = "#53215", status = "Open", subject = "Dispute about a recent trip"),
        SupportTicket(ticketID = "#53214", status = "Closed", subject = "Refund for a cancelled order"),
        SupportTicket(ticketID = "#53213", status = "Open", subject = "Problem with the app's GPS")
    )
}