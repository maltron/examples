## Event's Sample Data
insert into TICKET(NAME, QUANTITY_AVAILABLE) values('Male', 100);
insert into TICKET(NAME, QUANTITY_AVAILABLE) values('Female', 250);
insert into EVENT(DESIGNATION, EDITION, TITLE, LOCATION, ADDRESS, CITY, REGION, ZIP_CODE, COUNTRY, EVENT_STARTS, EVENT_ENDS, DESCRIPTION, ORGANIZER) values('myevent', 2015, 'My Event 2015 - The Example Event for all People, edition 2015', 'The Example Convention Center', '505 Maple Street', 'Example Town', 'Utah', '95550', 'Brazil', '2016-10-26 12:00:00', '2016-10-28 18:00:00', 'This is a small example of a description regarding a event', 1);
insert into EVENT_SELLS_TICKETS(EVENT_ID, TICKET_ID) values(1,1);
insert into EVENT_SELLS_TICKETS(EVENT_ID, TICKET_ID) values(1,2);