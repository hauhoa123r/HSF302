// Member specific JavaScript
$(document).ready(function() {
    console.log('Member JS loaded successfully');
    
    // Test sidebar functionality
    $('#sidebar-collapse').on('click', function() {
        console.log('Sidebar toggle clicked');
        $('#sidebar').toggleClass('active');
    });
    
    // Dropdown functionality (only for elements with dropdown-toggle class)
    if (!$('.dropdown-toggle').data('initialized')) {
        $('.dropdown-toggle').on('click', function(e) {
            console.log('Dropdown toggle clicked');
            e.preventDefault();
            var $this = $(this);
            var $parent = $this.parent();
            var $submenu = $this.next('ul');
            
            // Close other dropdowns
            $('.components li').not($parent).removeClass('active');
            $('.components ul').not($submenu).removeClass('show');
            
            // Toggle current dropdown
            $parent.toggleClass('active');
            $submenu.toggleClass('show');
            
            // Update aria-expanded
            var isExpanded = $submenu.hasClass('show');
            $this.attr('aria-expanded', isExpanded);
        }).data('initialized', true);
    }
    
    // Regular link navigation (for non-dropdown links)
    $('#sidebar ul li a:not(.dropdown-toggle)').on('click', function(e) {
        console.log('Regular link clicked:', $(this).attr('href'));
        // Allow normal navigation for regular links
        // No preventDefault() here
    });
}); 