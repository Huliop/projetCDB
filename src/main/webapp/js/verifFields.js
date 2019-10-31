$('#discontinued').change(function() {
	if ($('#discontinued').val() != "") {
		if (verifDate('#discontinued')) {
			$('#erreurDiscontinuedFormat').hide();
			$('#submit').prop('disabled', false);
			verifDiscontinued("#introduced")
		}
		else {
			$('#erreurDiscontinuedFormat').show();
			$('#submit').prop('disabled', true);
		}
    }
	else {
		$('#erreurDiscontinued').hide()
		$('#erreurDiscontinuedFormat').hide();
		$('#submit').prop('disabled', false);
	}
})

$('#introduced').change(function() {
	if ($('#introduced').val() != "") {
		if (verifDate('#introduced')) {
			$('#erreurIntroduced').hide();
			$('#submit').prop('disabled', false);
			verifDiscontinued("#discontinued")
		}
		else {
			$('#erreurIntroduced').show();
            $('#submit').prop('disabled', true);
		}
	}
	else {
        $('#erreurDiscontinued').hide()
        $('#erreurIntroduced').hide();
        $('#submit').prop('disabled', false);
    }
})

function verifDate(param) {
	if (moment($(param).val()).isBefore(moment("2038-01-19")) && moment($(param).val()).isAfter(moment("1970-01-01"))) {
		return true
	}
	return false;
}

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