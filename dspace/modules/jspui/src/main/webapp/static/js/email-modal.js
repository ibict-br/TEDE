/**
 * Handle e-mail dialog form
 */
jQuery(document).ready(function($){
	
	/** User requests sharing **/
	$("#sendEmail").click(function(){
		$("#actionResult").hide();
		$("#emailDialog").dialog("open");
		
	});

	$("#closeModal").click(function(){
		
		$("#sendEmailForm")[0].reset();
		$("#emailDialog").dialog("close");
		
	});
	
	/** User requests email sending **/
	$("#submitSend").click(function(){
		$("#urlToShare").val(window.location.href);
		$.post("search/shareviaemail", $("#sendEmailForm").serialize())
		.done(function(data) {
			$("#actionResult").show();
			$("#actionResult").html(data);
			
		}).fail(function() {
		});
	});
	
	$("#emailDialog").dialog({modal: false, autoOpen: false, width: 600, closeOnEscape: true, draggable: false, resizable: false});
	
});