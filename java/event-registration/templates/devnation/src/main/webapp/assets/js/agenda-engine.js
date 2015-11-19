/// Agenda Engine (WIP)
/// by Jonathan Rubinger
/// A tool for populating and filtering a session agenda from a JSON list and HTML template
/// Expects a JSON in Sched format (see Sched API)

sessionList = null;

$(document).ready(function() {
    var sessionListURL = "http://www.devnation.org/rest/sessions/all";
    var speakerListURL = "http://www.devnation.org/rest/users/all";
    var sessionTypesToShow = [ "Session", "Hands-on Lab", "BoF", "Session, Taste of Summit", "Open to All DevNation" ];
    
    getSessions();
    
    function getSessions () {
    
        $.get(sessionListURL, {  }, function(data) {
            data.sort(compareByTimeThenRoom);
            sessionList = data;
            for (var k in sessionList) {
                sessionList[k].friendly_event_start = getFriendlyTime(sessionList[k].event_start);
                sessionList[k].friendly_event_end = getFriendlyTimeOfDay(sessionList[k].event_end);
                sessionList[k].isLab = sessionList[k].event_type=="Hands-on Lab" ? true : false;
            }
            mergeSpeakers();
        });
    }
    
    function mergeSpeakers() {
        
        $.get(speakerListURL, {  }, function(data) {
        
            // Make hash maps so we can easily look up speaker data by name
            var avatarHash = {};
            var companyHash = {};
            var positionHash = {};
            var aboutHash = {};
            var urlHash = {};
            for (var s in data) {
                avatarHash[data[s].name] = data[s].avatar;
                companyHash[data[s].name] = data[s].company;
                positionHash[data[s].name] = data[s].position;
                aboutHash[data[s].name] = data[s].about;
                urlHash[data[s].name] = data[s].url;
            }

            for (var k in sessionList) {
                
                // Separate single 'speakers' string into array
                sessionList[k].speaker = {};
                if (typeof sessionList[k].speakers !== 'undefined') {

                    var sessionSpeakerList = sessionList[k].speakers.split(", ");
                    
                    for (var s in sessionSpeakerList) {
                        var n = sessionSpeakerList[s];
                        var spkr = { };
                        spkr.name = n;
                        spkr.avatar = avatarHash[n];
                        spkr.company = companyHash[n];
                        spkr.position = positionHash[n];
                        spkr.about = aboutHash[n];
                        spkr.url = urlHash[n];
                        
                        sessionList[k].speaker[s] = spkr;
                    }
                }
            }
            
            populate();
        });
    }

    function getFriendlyTimeObject (startTime) {

            // Reformats a string "2015-06-22 11:30:00" into "Monday, 22 June, 11:30AM"

            // Grab the day and month names
            var dayName = getDay(startTime);
            var monthName = getMonthName(startTime);

            // split into array [ '2015-06-22', '11:30:00' ],
            var dateAndTime = startTime.split(" ");

            // grab the first piece for the date, split by '-'
            // so [0] is year, [1] is month, [2] is date
            var datePieces = dateAndTime[0].split("-");
            var date = datePieces[2];

            // grab the second piece for the time, split by colons
            // so [0] is hours, [1] is minutes
            var timePieces = dateAndTime[1].split(":");

            var suffix;
            if (timePieces[0] >= 12) {
                suffix = "PM";
            }
            else {
                suffix = "AM";
            }
            var hour = ((Number(timePieces[0]) + 11) % 12 + 1);
            var minutes = timePieces[1];

            var obj = new Object();
            obj.dayName = dayName;
            obj.monthName = monthName;
            obj.date = date;
            obj.hour = hour;
            obj.minutes = minutes;
            obj.suffix = suffix;

            return obj;
        }
    
    function getFriendlyTime (startTime) {
        
        // Reformats a string "2015-06-22 11:30:00" into "Monday, 22 June, 11:30AM"
        
        obj = getFriendlyTimeObject(startTime);
        
        return (obj.dayName + ", " + obj.monthName + " " + obj.date + ", " + obj.hour + ":" + obj.minutes + obj.suffix);
    }

    function getFriendlyTimeOfDay (startTime) {
            obj = getFriendlyTimeObject(startTime);

            return (obj.hour + ":" + obj.minutes + obj.suffix);
        }

    function getFriendlyDay (startTime) {

            obj = getFriendlyTimeObject(startTime);

            return (obj.dayName + ", " + obj.monthName + " " + obj.date);
        }

    function populate () {
    
        var allSessions = "";
        var lastDay = "";
        
        for(var k in sessionList) {

            if (sessionTypesToShow.indexOf(sessionList[k].event_type) != -1 && sessionList[k].active === "Y") {
                
                var day = getFriendlyDay(sessionList[k].event_start);
                if (day != lastDay) {
                    allSessions += "<h2>" + day + "</h2>";
                    lastDay = day;
                }
                var template = $.templates("#sessionTemplate");
                var session = template.render(sessionList[k]);
                allSessions += session;
            }
        }
        
        $("#session-list").append(allSessions);
        
        var sessionEvent = $.Event("sessionsReady");
        $(document).trigger(sessionEvent);
    }
    
    function getMonthName (timeStamp) {
    // assumes timeStamp contains string formatted "YYYY-MM-DD"
    
        if (timeStamp.indexOf("-01-") != -1) {
            return "January";
        }
        if (timeStamp.indexOf("-02-") != -1) {
            return "February";
        }
        if (timeStamp.indexOf("-03-") != -1) {
            return "March";
        }
        if (timeStamp.indexOf("-04-") != -1) {
            return "April";
        }
        if (timeStamp.indexOf("-05-") != -1) {
            return "May";
        }
        if (timeStamp.indexOf("-06-") != -1) {
            return "June";
        }
        if (timeStamp.indexOf("-07-") != -1) {
            return "July";
        }
        if (timeStamp.indexOf("-08-") != -1) {
            return "August";
        }
        if (timeStamp.indexOf("-09-") != -1) {
            return "September";
        }
        if (timeStamp.indexOf("-10-") != -1) {
            return "October";
        }
        if (timeStamp.indexOf("-11-") != -1) {
            return "November";
        }
        if (timeStamp.indexOf("-12-") != -1) {
            return "December";
        }
        
        return "Unknown month";
    }
    
    function getDay (timeStamp) {
        
        if (timeStamp.indexOf("2015-06-21") != -1) {
            return "Sunday";
        }
        if (timeStamp.indexOf("2015-06-22") != -1) {
            return "Monday";
        }
        if (timeStamp.indexOf("2015-06-23") != -1) {
            return "Tuesday";
        }
        if (timeStamp.indexOf("2015-06-24") != -1) {
            return "Wednesday";
        }
        if (timeStamp.indexOf("2015-06-25") != -1) {
            return "Thursday";
        }
        
        return "Unknown Day";
    }
    
    function compareByTimeThenRoom(a, b) {

        // Sorts by event_start first, then by venue
        startTimeComparison = a.event_start.localeCompare(b.event_start);
        if (startTimeComparison != 0) {
            return startTimeComparison;
        }
        
        roomComparison = a.venue.localeCompare(b.venue);
        return roomComparison;
    }
    

});