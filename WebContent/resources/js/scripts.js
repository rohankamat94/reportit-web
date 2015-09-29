function loadMDL() {

	$('.ui-fileupload-content').css("visibility", "hidden");
	$('.remove')
			.removeClass()
			.addClass(
					'mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent')
			.hover(function() {
				console.log("in hover");
				$(this).removeClass('ui-state-hover')
			});

	$('.ui-dialog-titlebar').removeClass().addClass('mdl-card__title');
	$('.ui-dialog-title').removeClass().addClass('mdl-card__title-text');
	$('table').addClass('mdl-data-table mdl-js-data-table')
			.css('width', '100%');
}
function upload() {
	console.log("in upload");
	$(PF('uploadUser').jqId).find('*').click();
	console.log('exiting')
}

function showSnackBar(message) {
	console.log('in show' + message);
	$('.mdl-snackbar-text').text(message);
	$('#snackbar').css('visibility', 'visible');
	setTimeout(function() {
		$('#snackbar').css('visibility', 'hidden');
	}, 5000);
}