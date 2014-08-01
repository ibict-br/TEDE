(function() {
	
	jQuery.noConflict();
	jQuery.datepicker.setDefaults( jQuery.datepicker.regional[ "pt_BR" ] );
	jQuery(document).ready(function($) {
        $(".submit-date-field").datepicker({
        	dateFormat: "yy-mm-dd"
        });
        
    });
    
})();