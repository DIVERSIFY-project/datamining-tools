<?php

class WPWorker extends Worker {

	public function __construct(){
		include_once("MysqlWrapper.php");
		$this->mysqlWrapper = new MysqlWrapper();
	}
}

class WPWork extends Stackable {

	/**
	 * @var MysqlWrapper $mysqlWrapper
	 */
	protected $mysqlWrapper;
	protected $site;
	public function __construct(){

	}

	public function run() {

		$this->mysqlWrapper = $this->worker->mysqlWrapper;
		$this->site = $this->mysqlWrapper->getFreeSite($this->worker->getThreadId());

		if($this->site !== NULL){
			$this->plugin = $this->mysqlWrapper->getPlugin($this->site['plugins_checked_count'] + 1);
			if($this->plugin !== NULL){
				$res = $this->check();
				$this->mysqlWrapper->updateDB($res, $this->site['id'], $this->plugin['id']);

				if($res === true)
					echo "TRUE  ....  ( ".$this->site['id']." )  ....  ( ".$this->plugin['id']." )\n";
				else
					echo "FALSE  ...  ( ".$this->site['id']." )  ....  ( ".$this->plugin['id']." )\n";
			}else{
				echo "Plugin is NULL\n";
				sleep(rand(5, 50));
			}
		}else{
			echo "Site is NULL\n";
			sleep(rand(5, 50));
		}
	}

	protected function check($check_count = 0){

		if($check_count < 5)
			$url = $this->site['site']."/wp-content/plugins/".$this->plugin['plugin']."/readme.txt";
		elseif($check_count < 7)
			$url = $this->site['site']."/wp-content/plugins/".$this->plugin['plugin']."/".$this->plugin['plugin'].".php";
		elseif($check_count < 9)
			$url = $this->site['site']."/wp-content/plugins/".$this->plugin['plugin']."/".$this->plugin['plugin'].".css";
		else
                        $url = $this->site['site']."/wp-content/plugins/".$this->plugin['plugin']."/".$this->plugin['plugin'].".js";

		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
		curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 5);
		curl_setopt($ch, CURLOPT_TIMEOUT, 15);
		$page = curl_exec($ch);
		$info = curl_getinfo($ch);
		curl_close($ch);

		if($info['http_code'] == 200 && !empty($page) && ($info['redirect_count'] == 0 || strpos($info['url'], $this->plugin['plugin']) !== false)){
			return true;
		}else{
			if($check_count < 10){
				sleep(3);
				return $this->check(++$check_count);
			}else{
				return false;
			}
		}
	}
}
