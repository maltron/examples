// Additional application functions for DevNation website

// Global variables.
var pageHeight = $(window).height();
var pageWidth = $(window).width();
    
    
// Change width/height vars on resize
$(window).resize(function () {
   pageWidth = $(window).width();
   pageHeight = $(window).height();
});

// Centralize all objects.
centralized();

// Centralize after resize.
$(window).bind('resize', function() {
    centralized();
});
    

$(function() {
    if ($('.pages#agenda').length > 0) {
        runAgendaTable();
        
        // Add click event fix for plus/minus signs in agenda panel groups
        plusMinusAdjust();
    }
    
    // If this is the day-by-day agenda page, run the height matching function for the carousel slides
    if ($('.day-by-day').length > 0) {
        // Run carousel fix function
        matchCarouselSlideHeights('.day-by-day');
    }
});

// Function that changes a plus/minus sign in the newer session resource displays if the title link is clicked
function plusMinusAdjust() {
   $('.resource-review a[data-toggle="collapse"]').click(function(e) {
      e.preventDefault();
      
      // Detect whether this is a plus/minus symbol link or not
      if ($(this).hasClass('panel-show-hide-a') != true) {
         // This is the non symbol link (aka the linked session title), so change link
         var dataParent = $(this).attr('data-parent');
         // Find the symbol link
         var cl = 'collapsed';
         
         $tar = $('a[data-parent="'+dataParent+'"].panel-show-hide-a');
         if($tar.hasClass(cl) != true){
            $tar.addClass(cl);
         } else
         if ($tar.hasClass(cl)) {
            $tar.removeClass(cl);
         }
      }
   });
   
}

// Init video modal button functions
$(function() {
   /*
    <iframe width="640" height="480" src="//www.youtube.com/embed/otphq5MuVqA"
                                    frameborder="0" allowfullscreen></iframe>
    */
   $('.vid-modal').click(function(e){
      e.preventDefault();
      // URL
      var vidLink = $(this).attr('data-video-url');
      // Title
      var vidTitle = $(this).attr('data-video-title');
      
      
      // Fill Content
      var v = ''; // object to place in body
      if (vidLink) {
         v += '<iframe width="640" height="480" ';
         v += ' src="'+vidLink+'" ';
         v += ' frameborder="0" allowfullscreen></iframe><div class="space"></div>';
      } else
      if (!vidLink) {
         v = 'Video Not Available.';
      }
      
      $('#vidModal .modal-body').html(v);
      $('#vidModal .modal-title').html(vidTitle);
      
      
      // Set Modal
      $('#vidModal').modal('toggle');
         
      
      
      
   });
   
   $('.video-close').click(function(e){
      e.preventDefault();
      // Empty contents
      $('#vidModal .modal-body').empty();
      $('#vidModal .modal-title').empty();
      
      // Set Modal
      $('#vidModal').modal('toggle');
         
      });
   
   
});


// Function control for agenda/schedule display table (Agenda 'table' is not an HTML <table> but a series of Bootstrap rows)
function runAgendaTable() {
    
    
    /*
     * Gather each track slot by class .pos* (.pos1,.pos2,.pos3,.pos4,.pos5)
     * Each track label will go next to appropriate slot at smaller viewport widths (mainly phones)
     */
    
    $('.track-listings').children('div.span2').each(function(i,e){
            if (!$(e).hasClass('time-slot-tracks')) {
                var u = $(e).html();
                // Identify which position
                var c = $(e).attr('class');
                c = c.replace('span2',''); // Remove .span2 (.span2 could change but this is the easiest for now)
                c = removeWhitespace(c);  // Remove whitespace
                var d = '<span class="track-left">'+u+'</span>';
                
                // Insert this value into every .day-block div with the same .pos* class
                $('.day-block').children('div.row').children('div.span2.'+c+'').each(function(){
                        // First take each "cell's" content (not a table; actually small divs) and place contents in right side <span>
                        var g = $(this).html();
                        g = '<span class="track-right">'+removeWhitespace(g)+'</span>';
                        $(this).html(g);
                        
                        $(this).prepend(d);
                    })
            }
        });
    
    
    // Place tracks throughout table.
    var tracks = $('.track-listings').clone();
    // Remove tracks row at very top.
    $('.track-listings').remove();
    // Place tracks clone just after each event date.
    $('.event-day').after($(tracks));
    
    // FOR THE AT-A-GLANCE VIEWS
    if ($('.panel-body table').length > 0) {
        // On load
        setPanelHeadingHeights();
        // ... and on any window resize.
        $(window).resize(function() {
           setPanelHeadingHeights();
        });
        
    }
}


// Agenda function for resetting panel link heights so text doesn't overlap
function setPanelHeadingHeights() {
    $('.panel-heading').each(function(i,e){

            if ($(this).children('a[data-toggle="collapse"]').length > 0) {
                var aHeight = $(this).children('a[data-toggle="collapse"]').height();
                $(this).css({'height':aHeight+'px'});
            }
    });
}


// General whitespace removal function
function removeWhitespace(x) {
    // Value to be returned
    var y;
    // Regex to remove whitespace
    y = x.replace(/(^\s+|\s+$)/g, '');
    // Return value
    return y;
}


// FOR SLIDE CAROUSELS: A FUNCTION THAT MATCHES HEIGHTS OF EACH SLIDE
function matchCarouselSlideHeights(tar) {
    
    var c = $(tar).children('div.item')
    
    // Build an array of the heights of each carousel slide
    var heightArr = new Array();
    // Add heights to array
    c.each(function(i,e){
        heightArr.push($(e).outerHeight());        
    });
    
    // Determine the greatest height
    var maxHeight = parseInt(Math.max.apply(Math, heightArr));
    
    // Now apply that height to each slide so the appearance is uniform
    c.each(function(i,e){
        $(e).css({'height': maxHeight+'px'});
    });
 }


// CENTRALIZING AND FADING IN THE WELCOME CONTENT
/* Modified from plugins/jquery.centralized.js (no longer directly incorporated) */
function centralized(){
    var config = {
        'delay': 400,
        'fadeSpeed': 300
    };
    
    
    var obj          = $('.centralized');
    
    var cutOffWidth  = 610;
    
    // Test window width for mobile size
    if (pageWidth > cutOffWidth) {
        //code
    
            return obj.each(function(){
            
                setTimeout(function() {
                    
                    obj.css("margin-top", "-" + Math.max(0, (obj.outerHeight() / 2)) + "px");
                    obj.css("margin-left", "-" + Math.max(0, (obj.outerWidth() / 2)) + "px");
                    obj.css('visibility','visible');
                    obj.fadeTo(500, 1);
                    
               
                }, config.delay);
            });
    }
    else
    if (pageWidth <= cutOffWidth) {
        $(function(){
            console.log(obj.outerWidth());
            obj.css("margin-top", "-" + Math.max(0, (obj.height() / 2)) + "px");
            obj.css("margin-left", "-" + Math.max(0, (obj.width())) + "px");
            obj.addClass('centralized-non-animated');
        });
    }
};



