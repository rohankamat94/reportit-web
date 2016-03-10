function loadMDL() {
	createCardFromDialog();
}

function createCardFromDialog() {
	$('.ui-dialog-titlebar').removeClass().addClass('mdl-card__title');
	$('.ui-dialog-title').removeClass().addClass('mdl-card__title-text');
}

function blur() {
	$('.mdl-layout__container').css('opacity', '0.4');
	$('#toast').css('opacity', '1'); // show toast in upload user dialog
}

function unblur() {
	$('.mdl-layout__container').css('opacity', '1');
}

/*
 * function makeButtonMdl() { $('.remove') .removeClass() .addClass( 'mdl-button
 * mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent'); }
 */
function showLoader() {
	document.getElementById('loadDialog').showModal();
}

function hideLoader() {
	document.getElementById('loadDialog').close();
}

function upload() {
	console.log("in upload");
	$(PF('uploadUser').jqId).find('*').click();
	console.log('exiting');
}

function showSnackBar(mes) {
	var snackbarContainer = document.querySelector('#toast');
	var data = {
		message : mes,
		timeout : 2000
	}
	snackbarContainer.MaterialSnackbar.showSnackbar(data);
}
function showAddCategory() {
	upgradeComponents('.mdl-switch', '.mdl-textfield', '.mdl-button');
	PF('addDlg').show();
}
function showEditCategory() {
	upgradeComponents('.mdl-switch', '.mdl-textfield', '.mdl-button');
	PF('editDlg').show();
}

function upgradeComponents() {
	for ( var x in arguments) {
		console.log(arguments[x]);
		$(arguments[x]).each(function() {
			componentHandler.upgradeElement(this);
		});
	}
	console.log('upgraded');
}
function getRipple() {
	for ( var i in arguments) {
		console.log(arguments[i]);
		var comp = $(PF(arguments[i]).jqId).get();
		for ( var x in comp) {
			componentHandler.upgradeElements(comp[x]);
		}
	}
}

function setSelected(linkId) {
	$('.drawer-link').removeClass('selected-link');
	$('#' + linkId).addClass('selected-link');
}