jQuery.noConflict();

jQuery(document).ready(function(){
	
	jQuery("#preview-link").click(function(){
		
		jQuery('#myModal').show();
		jQuery('.reveal-modal-bg').show();
		
		jQuery('#myModal').foundation('reveal', 'open', {
			animation: 'fadeAndPop',
			animation_speed: 500,
			close_on_background_click: true,
			close_on_esc: true,
			close_on_background_click: true,
			dismiss_modal_class: 'close-reveal-modal',
			bg_class: 'reveal-modal-bg',
			root_element: 'body'
		});
		
		
		
	});
	
	jQuery("#close-reveal-modal").click(function(){
		
		jQuery('#myModal').hide();
		jQuery('.reveal-modal-bg').hide();
		
	});
	
});