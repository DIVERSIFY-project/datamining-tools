<?php
include_once("MultiThreading.php");
include_once("MysqlWrapper.php");

$wrapper = new MysqlWrapper();
$wrapper->syncPluginsData();
$wrapper->reset();
$pool = new Pool(500, "WPWorker");
$continue = true;
$dif_time = 1*60*60;
$i=0;

while($continue){
	$wrapper->reset();

	$start_time = time();
	$end_time = $start_time + $dif_time;

	while(time() < $end_time){
		$pool->submit(new WPWork());
		if($i % 20 == 0) sleep(1);
		if($i++ > 1000){
			sleep(30);
			$i = 0;
			$wrapper = new MysqlWrapper();
			$wrapper->reset();
			$continue = $wrapper->isWorkToDo();
		}
	}

	$pool->shutdown();
	$d = time() - $end_time;
	echo "\n\n\n\n\n\n-------shutdown after: ".$d."s----\n\n\n\n\n\n\n";
	$wrapper->syncPluginsData();
	sleep(10);
}
