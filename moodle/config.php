<?php  // Moodle configuration file

unset($CFG);
global $CFG;
$CFG = new stdClass();
$dbhost=$_SERVER['DBHOST'];
$dbname=$_SERVER['DBNAME'];
$dbuser=$_SERVER['DBUSER'];
$dbpass=$_SERVER['DBPASS'];
$wwwroot=$_SERVER['SERVER_NAME'].":".$_SERVER['SERVER_PORT'];

#$CFG->dbtype    = 'mysqli';
$CFG->dbtype    = 'mariadb';
$CFG->dblibrary = 'native';
$CFG->dbhost    = $dbhost;
$CFG->dbname    = $dbname;
$CFG->dbuser    = $dbuser;
$CFG->dbpass    = $dbpass;
$CFG->lang      = "en";
$CFG->prefix    = 'mdl_';
$CFG->dboptions = array (
  'dbpersist' => 0,
  'dbport' => '',
  'dbsocket' => '',
  'dbcollation' => 'utf8mb4_general_ci',
);

$CFG->wwwroot   = $wwwroot;
$CFG->dataroot  = '/moodledata';
$CFG->admin     = 'admin';

$CFG->directorypermissions = 0777;

require_once(__DIR__ . '/lib/setup.php');

// There is no php closing tag in this file,
// it is intentional because it prevents trailing whitespace problems!
