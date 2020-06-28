<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require './PHPMailer/Exception.php';
require './PHPMailer/PHPMailer.php';
require './PHPMailer/SMTP.php';

$curdate=date('d-m-Y H:i:s');

$Recipient="therealsteveshilling@gmail.com";
$username="TPS System Alert";
$sender="therealsteveshilling@gmail.com";
$message="The web site is reporting as being down at $curdate";
$Subject="Therapypages.com is down";

$email_from = 'tpsservicesltd@gmail.com';
$email_host = 'smtp.gmail.com';
$email_port = 587;
$email_auth = true;
$email_secure = 'tls';
$email_user = 'tpsservicesltd@gmail.com';
$email_pass = getenv('MAILPASS');

$headers = "From: " . $email_from . "\n";
$headers .= "Reply-To: ". $email_from . "\n";
#$headers .= "Originating-IP: ". $_SERVER["REMOTE_ADDR"] . "\n";
$headers .= "MIME-Version: 1.0\r\n";
$headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";

$Body="<HTML><BODY>\n";
$Body.="<p align=left>Message from $username as follows;</p>\n";
$Body.="<p>&nbsp;</p>\n";
$Body.="<p align=LEFT>Reply to: <a href='mailto:". $sender . "'>$sender</a>, who is $username</p>\n";
$Body.="<P>&nbsp;</P>\n";
$Body.="<P ALIGN=\"JUSTIFY\">$message</P>\n";
$Body.="</BODY></HTML>\n";

$mailer = new PHPMailer();
$mailer->isSMTP();
$mailer->SMTPAuth = $email_auth;
$mailer->SMTPSecure = $email_secure;
$mailer->Mailer = "smtp";
$mailer->Host = $email_host;
$mailer->Port = $email_port;
$mailer->isHTML(true);
$mailer->Username = $email_user;
$mailer->Password = $email_pass;
$mailer->setFrom($email_from,"TPS Service Ltd");
$mailer->addReplyTo($sender,"$username");
$mailer->Subject = $Subject;
$mailer->isHTML(true);
$mailer->Body = $Body;
$mailer->addAddress($Recipient,"Steve Shilling");
$mailer->SMTPDebug  = 0;

try {
	$mailer->send();
} catch (Exception $e) {
	echo "<h1>Error:</h1>";
	echo "Error: ".$mail->ErrorInfo;
	echo "Exception: ".$e->getMessage();
}
if ( $mailer->Send() ) {
	echo "Your message has been sent successfully";
} else {
	echo "Your has not been sent";
}
?>
