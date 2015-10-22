function loadMDL() {
	createCardFromDialog();
}

function createCardFromDialog() {
	$('.ui-dialog-titlebar').removeClass().addClass('mdl-card__title');
	$('.ui-dialog-title').removeClass().addClass('mdl-card__title-text');
}

/*
 * function makeButtonMdl() { $('.remove') .removeClass() .addClass( 'mdl-button
 * mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent'); }
 */
function showLoader() {
	PF('loadDlg').show();
}

function hideLoader() {
	PF('loadDlg').hide();
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

function getRipple() {
	for ( var i in arguments) {
		console.log(arguments[i]);
		var comp = $(PF(arguments[i]).jqId).get();
		for(var x in comp){
			componentHandler.upgradeElements(comp[x]);
		}
	}
}
