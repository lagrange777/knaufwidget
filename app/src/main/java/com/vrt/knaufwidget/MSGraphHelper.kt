package com.vrt.knaufwidget

import android.os.Build
import androidx.annotation.RequiresApi
import com.microsoft.graph.models.*
import com.microsoft.graph.options.HeaderOption
import com.microsoft.graph.options.Option
import com.microsoft.graph.options.QueryOption
import com.microsoft.graph.requests.EventCollectionPage
import com.microsoft.graph.requests.GraphServiceClient
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CompletableFuture


class MSGraphHelper {
    private var INSTANCE: MSGraphHelper? = null
    private var mClient: GraphServiceClient<*>? = null

    private fun GraphHelper() {
        val authProvider: AuthenticationHelper = AuthenticationHelper.getInstance()
        mClient = GraphServiceClient.builder()
            .authenticationProvider(authProvider).buildClient()
    }

    @Synchronized
    fun getInstance(): MSGraphHelper? {
        if (INSTANCE == null) {
            INSTANCE = MSGraphHelper()
        }
        return INSTANCE
    }

    fun getUser(): CompletableFuture<User?>? {
        // GET /me (logged in user)
        return mClient!!.me().buildRequest()
            .select("displayName,mail,mailboxSettings,userPrincipalName")
            .async
    }

    // <GetEventsSnippet>
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCalendarView(
        viewStart: ZonedDateTime,
        viewEnd: ZonedDateTime,
        timeZone: String
    ): CompletableFuture<List<Event?>>? {
        val options: MutableList<Option> = LinkedList<Option>()
        options.add(
            QueryOption(
                "startDateTime",
                viewStart.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )
        )
        options.add(
            QueryOption(
                "endDateTime",
                viewEnd.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )
        )

        // Start and end times adjusted to user's time zone
        options.add(
            HeaderOption(
                "Prefer",
                "outlook.timezone=\"$timeZone\""
            )
        )
        val allEvents: MutableList<Event> = LinkedList<Event>()
        // Create a separate list of options for the paging requests
        // paging request should not include the query parameters from the initial
        // request, but should include the headers.
        val pagingOptions: MutableList<Option> = LinkedList<Option>()
        pagingOptions.add(
            HeaderOption(
                "Prefer",
                "outlook.timezone=\"$timeZone\""
            )
        )
        return mClient!!.me().calendarView()
            .buildRequest(options)
            .select("subject,organizer,start,end")
            .orderBy("start/dateTime")
            .top(5)
            .async
            .thenCompose<Any> { eventPage: EventCollectionPage ->
                processPage(
                    eventPage,
                    allEvents,
                    pagingOptions
                )
            }
    }

    private fun processPage(
        currentPage: EventCollectionPage,
        eventList: MutableList<Event>,
        options: List<Option>
    ): CompletableFuture<List<Event?>>? {
        eventList.addAll(currentPage.currentPage)

        // Check if there is another page of results
        val nextPage = currentPage.nextPage
        return if (nextPage != null) {
            // Request the next page and repeat
            nextPage.buildRequest(options)
                .async
                .thenCompose<Any> { eventPage: EventCollectionPage ->
                    processPage(
                        eventPage,
                        eventList,
                        options
                    )
                }
        } else {
            // No more pages, complete the future
            // with the complete list
            CompletableFuture.completedFuture(eventList)
        }
    }

    // Debug function to get the JSON representation of a Graph
    // object
    fun serializeObject(`object`: Any): String? {
        return mClient!!.serializer!!.serializeObject(`object`)
    }
    // </GetEventsSnippet>

    // </GetEventsSnippet>
    // <CreateEventSnippet>
    @RequiresApi(Build.VERSION_CODES.O)
    fun createEvent(
        subject: String,
        start: ZonedDateTime,
        end: ZonedDateTime,
        timeZone: String,
        attendees: Array<String?>,
        body: String
    ): CompletableFuture<Event?>? {
        val newEvent = Event()

        // Set properties on the event
        // Subject
        newEvent.subject = subject

        // Start
        newEvent.start = DateTimeTimeZone()
        // DateTimeTimeZone has two parts:
        // The date/time expressed as an ISO 8601 Local date/time
        // Local meaning there is no UTC or UTC offset designation
        // Example: 2020-01-12T09:00:00
        newEvent.start!!.dateTime = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        // The time zone - can be either a Windows time zone name ("Pacific Standard Time")
        // or an IANA time zone identifier ("America/Los_Angeles")
        newEvent.start!!.timeZone = timeZone

        // End
        newEvent.end = DateTimeTimeZone()
        newEvent.end!!.dateTime = end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        newEvent.end!!.timeZone = timeZone

        // Add attendees if any were provided
        if (attendees.size > 0) {
            newEvent.attendees = LinkedList()
            for (attendeeEmail in attendees) {
                val newAttendee = Attendee()
                // Set the attendee type, in this case required
                newAttendee.type = AttendeeType.REQUIRED
                // Create a new EmailAddress object with the address
                // provided
                newAttendee.emailAddress = EmailAddress()
                newAttendee.emailAddress!!.address = attendeeEmail
                (newEvent.attendees as LinkedList<Attendee>).add(newAttendee)
            }
        }

        // Add body if provided
        if (body.isNotEmpty()) {
            newEvent.body = ItemBody()
            // Set the content
            newEvent.body!!.content = body
            // Specify content is plain text
            newEvent.body!!.contentType = BodyType.TEXT
        }
        return mClient!!.me().events().buildRequest()
            .postAsync(newEvent)
    }
    // </CreateEventSnippet>
}