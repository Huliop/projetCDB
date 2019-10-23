$('#discontinued').change(function() {
	if ($('#discontinued').val() != "") {
        verifDiscontinued("#introduced")
    }
	else {
		$('#erreurDiscontinued').hide()
		$('#submit').prop('disabled', false);
	}
})

$('#introduced').change(function() {
	if ($('#introduced').val() != "") {
		verifDiscontinued("#discontinued")
	}
	else {
        $('#erreurDiscontinued').hide()
        $('#submit').prop('disabled', false);
    }
})

function verifDiscontinued(param) {
    if ($(param).val() != "") {
        var discontinued = moment($('#discontinued').val());
        var introduced = moment($('#introduced').val());
        if (!discontinued.isAfter(introduced)) {
            $('#erreurDiscontinued').show();
            $('#submit').prop('disabled', true);
        }
        else {
            $('#erreurDiscontinued').hide();
        }
    }
}

$('#computerName').keyup(function() {
	if($('#computerName').val() != "") {
		$('#erreurName').hide();
		$('#submit').prop('disabled', false);
	}
	else {
		$('#erreurName').show();
		$('#submit').prop('disabled', true);
	}
})