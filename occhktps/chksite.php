<?php
$content=file_get_contents("http://www.therapypages.com");
$search = <<<EOF
<meta name="author" content="TPS Services Ltd">
EOF;

if (strpos($content, $search) == FALSE) {
	echo "ALERT: Therapypages is down!\n";
} else {
	echo "Therapypages is OK\n";
}
?>
