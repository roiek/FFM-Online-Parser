function parseFile() {
	$('#fileInput').simpleUpload("/", {

		success: function (data) {
			$("#result").html(data);
		},
		error: function (error) {
			$("#result").html(data);
		}
	});
}