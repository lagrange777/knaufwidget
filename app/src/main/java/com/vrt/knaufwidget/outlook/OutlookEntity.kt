package com.vrt.knaufwidget.outlook

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class OutlookEntity(
    @SerialName("@odata.context")
    val serviceData: String,
    @SerialName("value")
    val info : List<OutlookEventEntity>
)

/**
 * {
"@odata.etag": "W/\"n42sM5q8nEaUnDXd95ENcgAAAAAGSg==\"",
"id": "AQMkADAwATMwMAItNTEwZi0yMgA4Ni0wMAItMDAKAEYAAAP24D9_auz5QKJadkv6BbNEBwCfjawzmrycRpScNd33kQ1yAAACAQ0AAACfjawzmrycRpScNd33kQ1yAAACFQAAAQ==",
"createdDateTime": "2022-07-11T11:25:38.7888006Z",
"lastModifiedDateTime": "2022-07-11T11:27:39.1678772Z",
"changeKey": "n42sM5q8nEaUnDXd95ENcgAAAAAGSg==",
"categories": [],
"transactionId": "590bc323-7f90-b6ba-0f20-a4e46ae3375b",
"originalStartTimeZone": "Russian Standard Time",
"originalEndTimeZone": "Russian Standard Time",
"iCalUId": "040000008200E00074C5B7101A82E00800000000868183F21895D801000000000000000010000000645FDE58771E3C43BF60AFF4138A8D68",
"reminderMinutesBeforeStart": 15,
"isReminderOn": true,
"hasAttachments": false,
"subject": "sdfsdfsd",
"bodyPreview": "sdfdsdfs",
"importance": "normal",
"sensitivity": "normal",
"isAllDay": false,
"isCancelled": false,
"isOrganizer": true,
"responseRequested": true,
"seriesMasterId": null,
"showAs": "busy",
"type": "singleInstance",
"webLink": "https://outlook.live.com/owa/?itemid=AQMkADAwATMwMAItNTEwZi0yMgA4Ni0wMAItMDAKAEYAAAP24D9%2Bauz5QKJadkv6BbNEBwCfjawzmrycRpScNd33kQ1yAAACAQ0AAACfjawzmrycRpScNd33kQ1yAAACFQAAAQ%3D%3D&exvsurl=1&path=/calendar/item",
"onlineMeetingUrl": null,
"isOnlineMeeting": false,
"onlineMeetingProvider": "unknown",
"allowNewTimeProposals": true,
"occurrenceId": null,
"isDraft": false,
"hideAttendees": false,
"responseStatus": {
"response": "organizer",
"time": "0001-01-01T00:00:00Z"
},
"body": {
"contentType": "html",
"content": "<html>\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n</head>\r\n<body>\r\n<div class=\"elementToProof\" style=\"font-family:Calibri,Arial,Helvetica,sans-serif; font-size:12pt; color:rgb(0,0,0)\">\r\nsdfdsdfs</div>\r\n</body>\r\n</html>\r\n"
},
"start": {
"dateTime": "2022-07-25T05:00:00.0000000",
"timeZone": "UTC"
},
"end": {
"dateTime": "2022-07-25T05:30:00.0000000",
"timeZone": "UTC"
},
"location": {
"displayName": "",
"locationType": "default",
"uniqueIdType": "unknown",
"address": {},
"coordinates": {}
},
"locations": [],
"recurrence": null,
"attendees": [],
"organizer": {
"emailAddress": {
"name": "outlook_1792B91F017EF01E@outlook.com",
"address": "outlook_1792B91F017EF01E@outlook.com"
}
},
"onlineMeeting": null,
"calendar@odata.associationLink": "https://graph.microsoft.com/v1.0/users('outlook_1792B91F017EF01E@outlook.com')/calendars('AQMkADAwATMwMAItNTEwZi0yMgA4Ni0wMAItMDAKAEYAAAP24D9_auz5QKJadkv6BbNEBwCfjawzmrycRpScNd33kQ1yAAACAQYAAACfjawzmrycRpScNd33kQ1yAAACCT0AAAA=')/$ref",
"calendar@odata.navigationLink": "https://graph.microsoft.com/v1.0/users('outlook_1792B91F017EF01E@outlook.com')/calendars('AQMkADAwATMwMAItNTEwZi0yMgA4Ni0wMAItMDAKAEYAAAP24D9_auz5QKJadkv6BbNEBwCfjawzmrycRpScNd33kQ1yAAACAQYAAACfjawzmrycRpScNd33kQ1yAAACCT0AAAA=')"
},
 */
@Serializable
data class OutlookEventEntity(
    @SerialName("@odata.etag")
    val serviceInfo: String,
    val id: String,
    val createdDateTime: String,
    val lastModifiedDateTime: String,
    val changeKey: String,
    val transactionId: String,
    val originalStartTimeZone: String,
    val originalEndTimeZone: String,
    val iCalUId: String,
    val reminderMinutesBeforeStart: Long,
    val isReminderOn: Boolean,
    val hasAttachments: Boolean,
    @SerialName("subject")
    val title: String,
    @SerialName("bodyPreview") val subtitle: String,
    val importance: String,
    val sensitivity: String,
    val isAllDay: Boolean,
    val isCancelled: Boolean,
    val isOrganizer: Boolean,
    val responseRequested: Boolean,
    val showAs: String,
    val type: String,
    val webLink: String,
    val isOnlineMeeting: Boolean,
    val onlineMeetingProvider: String,
    val allowNewTimeProposals: Boolean,
    val isDraft: Boolean,
    val hideAttendees: Boolean,
    val responseStatus: ResponseStatusEntity,
    val body: BodyEntity,
    val start: ScheduleEntity,
    val end: ScheduleEntity,
    ) {
    @Transient
    var day: Int = start.dateTime.split("-")[2].substring(0, 2).toInt()
    @Transient
    var month: Int = start.dateTime.split("-")[1].toInt()
    @Transient
    var year: Int = start.dateTime.split("-")[0].toInt()
}

@Serializable
data class ResponseStatusEntity(val response: String, val time: String)

@Serializable
data class BodyEntity(val contentType: String, val content: String)

@Serializable
data class ScheduleEntity(val dateTime: String, val timeZone: String)

