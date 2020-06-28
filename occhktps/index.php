<?php
$content=file_get_contents("http://www.therapypages.com");
$min=60000;
$timewaitsecs=60000/6;
$search = <<<EOF
<meta name="author" content="TPS Services Ltd">
EOF;

if (strpos($content, $search) == FALSE) {
	echo "ALERT: Therapypages is down!<br>";
	echo date('d-m-Y H:i:s');
} else {
	echo "Therapypages is OK<br>";
	echo date('d-m-Y H:i:s');
}
echo "<script>
function loadagain() {
	document.location = 'index.php'
}
setTimeout(loadagain,$timewaitsecs)
</script>";
#header("Refresh:$timewaitsecs");
?>
