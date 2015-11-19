// JS file for controlling the appearance of panels and sub-panels based on submitted URL hashes

// Init
$(function() {
    collectHash();
});

function collectHash() {
    // All hash names in URL string
    var hashName = window.location.hash;
    // Target <a> check variable
    
    // The target hash's parent should always be a sub-panel, meaning its parent's class would be 'panel-heading'
    if ($('a[href='+hashName+']').parent('div').hasClass('panel-heading') == true) {
        // If yes, then panel handling can proceed...
        var hashId   = hashName.slice(1); // This removes the leading '#'
        
        // 1. Remove 'collapsed' class name form target panel.
        $('a[href='+hashName+']').removeClass('collapsed');
        
        // 2. Find target panel and add 'in' as class name to open panel in body.
        $('div.panel-collapse'+hashName).addClass('in');
        
        // 3. Find parent section and set as open (if it isn't open already).
        var containerId = $('a[href='+hashName+']').parents('div.panel-group.agenda-display').attr('id');
        
         
        if ($('a[data-parent=#'+containerId+']').hasClass('collapse') == false) {
            
            // First, get target panel ID
            var tarIdHref = $('a[data-parent=#'+containerId+']').attr('href');
            console.log(tarIdHref);
            tarIdHref = tarIdHref.slice(1);

            $('a[data-parent=#'+containerId+']').removeClass('collapsed');
            
            // Now set panel to be open by adding 'in' to its class
            $('div#'+tarIdHref).addClass('in');
            
        }
        
        
        // 4. Determine slight scroll Y change
        var newY = $('a[href='+hashName+']').offset().top - 60; // Remove 50 px from location to account for navbar
        // Add short timeout to help ensure anchor jump doesn't occur until after page load.    
        setTimeout(function() {
           $.scrollTo(newY);
        },200);
        
    }
    
}