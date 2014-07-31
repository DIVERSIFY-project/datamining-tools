<?php

function sqlQuery($query){
	$mysql = new mysqli("127.0.0.1","root","ubuntu","wp");
	$res = $mysql->query($query);
	$mysql->close();

	return $res;
}

function getSites(){
	$res = sqlQuery("SELECT `site` FROM `all_sites` LIMIT 10000");
	if($res !== NULL)
		while($row = $res->fetch_assoc())
			$ret[] = $row;
	else    $ret = NULL;

	return $ret;
}

function getSite($id){
	$res = sqlQuery("SELECT `site` FROM `all_sites` WHERE `rank` = $id");

	if($res !== NULL)
		$ret = $res->fetch_assoc();
	else    $ret = array('stie' => NULL);

	return $ret['site'];
}

// List of 100 sites
$sites = array('wallst.com', 'mango.com');

// Path to the dir which is storing all crawled data
$dir_path = '/home/ubuntu/Workspace/jscss/sites/';

// Regex pattern for js & css paths in a html source file
$js_pat = "/<script[^>]+src=[\"|'](.+[^\"|']\.js).*?[\"|']/";
$css_pat = "/<link[^>]+href=[\"|'](.+[^\"|']\.css).*?[\"|']/";

// Initialize the CURL for crawling
$ch = curl_init();
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 5);
curl_setopt($ch, CURLOPT_ENCODING, '');
curl_setopt($ch, CURLOPT_TIMEOUT, 30);

// Number of sites already tested
$l = intval(shell_exec('ls /home/ubuntu/Workspace/jscss/sites/ | wc -l'));

// Do it for each site from the list
for($i=$l; $i<=100000; $i++){
	$site = getSite($i);

	// Crawl the main page of the current site
	curl_setopt($ch, CURLOPT_URL, $site);
	$page = curl_exec($ch);
	if($page !== ''){

	// Create folder for the current site
	$site_path = $dir_path.$site;
	if (!file_exists($site_path))
		mkdir($site_path);

	// Create folder for the js files
	if (!file_exists($site_path."/js"))
		mkdir($site_path."/js");
//	// Create folder for the css files
//	if (!file_exists($site_path."/css"))
//		mkdir($site_path."/css");


	// Getting paths to all js & css files
	preg_match_all($js_pat, $page, $js_paths);
	//preg_match_all($css_pat, $page, $css_paths);

	// Do it for each found js path
	foreach($js_paths[1] as $js_path){
		// Build the complete url
		if(substr($js_path, 0, 2) == "//")
			$url = substr($js_path, 2);
		elseif(substr($js_path, 0, 4) == "http")
			$url = $js_path;
		else
			$url = $site.$js_path;

		// Get the content of the current js file
	        curl_setopt($ch, CURLOPT_URL, $url);
        	$file_content = curl_exec($ch);

		$js_filename = end(explode("/",$js_path));
		file_put_contents($site_path."/js/".$js_filename, $file_content);
	}

//	// Do it for each found css path
//	foreach($css_paths[1] as $css_path){
//		// Build the complete url
//		if(substr($css_path, 0, 2) == "//")
//			$url = substr($css_path, 2);
//		elseif(substr($css_path, 0, 4) == "http")
//			$url = $css_path;
//		else
//			$url = $site.$css_path;
//
//		// Get the content of the current css file
//		curl_setopt($ch, CURLOPT_URL, $url);
//		$file_content = curl_exec($ch);
//
//		$css_filename = end(explode("/",$css_path));
//		file_put_contents($site_path."/css/".$css_filename, $file_content);
//	}

	}
	echo $i." --- ".$site."\n";
}

curl_close($ch);

?>
