<?php

class MysqlWrapper {
		private $con;

	public function __construct() {

	}

	private function openConnection(){
		$this->con = new mysqli("127.0.0.1","root","ubuntu","wp-data");
	}

	private function closeConnection(){
		$this->con->close();
	}

	public function query($query){
		try{
			$this->openConnection();
			$res = $this->con->query($query);
			$this->closeConnection();
		}catch(Exception $e){
			$this->closeConnection();
			echo "ERROR: ".$e->getMessage()."\n";
			$res = NULL;
		}

		return $res;
	}

	public function getSite($id){
		$query = "SELECT * FROM `wp_sites` WHERE `id` = ".$id;
		$res = $this->query($query);
		if($res !== NULL)
			$row = $res->fetch_array();
		else	$row = NULL;

		return $row;
	}

	public function getPlugin($id){
		$query = "SELECT * FROM `wp_plugins` WHERE `id` = ".$id;
		$res = $this->query($query);
		if($res !== NULL)
			$row = $res->fetch_array();
		else	$row = NULL;

		return $row;
	}

	public function getFreeSite($threadId){

		$maxSiteId = "SELECT (`id`*100) as 'maxSiteId' FROM `wp_plugins` WHERE `tested` != 0 ORDER BY `id` DESC LIMIT 1";
		$query = "
			UPDATE
				`wp_sites`
			SET
				`thread_id` = '$threadId'
			WHERE
					`thread_id` = '0'
				AND
					`plugins_checked_count` <= 1500
				AND
					`id` <= COALESCE(($maxSiteId), 100)
			ORDER
				BY
					`plugins_checked_count` ASC,
					`id` ASC
			LIMIT 1
		";

		$this->query($query);

		$query = "SELECT * FROM `wp_sites` WHERE `thread_id` = '$threadId'";
		$res = $this->query($query);
		if($res !== NULL)
			$row = $res->fetch_array();
		else	$row = NULL;

		return $row;
	}

	public function getMaxSiteId(){
		$query = "SELECT `id` FROM `wp_plugins` WHERE `tested` != 0 ORDER BY `id` DESC LIMIT 1";
	}

	public function reset(){
		$query = "UPDATE `wp_sites` SET `thread_id`='0'";
		$this->query($query);
	}

	public function updateDB($type, $site, $plugin){
		if($type === true){
			$query = "UPDATE `wp_plugins` SET `usage` = `usage`+1, `tested` = `tested`+1 WHERE `id` = $plugin";
			$this->query($query);

			$query = "
				UPDATE
					`wp_sites`
				SET
					`plugins_have` = CONCAT(`plugins_have`, '$plugin', ','),
					`plugins_have_count` = `plugins_have_count`+1,
					`plugins_checked` = CONCAT(`plugins_checked`,'$plugin', ','),
					`plugins_checked_count` = `plugins_checked_count`+1,
					`thread_id` = 0
				WHERE
					`id` = $site
			";
			$this->query($query);
		}else{
			$query = "UPDATE `wp_plugins` SET `tested` = `tested`+1 WHERE `id` = $plugin;";
			$this->query($query);

			$query = "
				UPDATE
					`wp_sites`
				SET
					`plugins_checked` = CONCAT(`plugins_checked`, '$plugin', ','),
					`plugins_checked_count` = `plugins_checked_count`+1,
					`thread_id` = 0
				WHERE
					`id` = $site
			";
			$this->query($query);
		}
	}

	public function isWorkToDo(){
		$query = "SELECT MIN(`plugins_checked_count`) as 'min' FROM `wp_sites`";

		$res = $this->query($query);
		if($res !== NULL)
			$row = $res->fetch_array();
		else	return true;

		if($row['min'] < 1500)
			return true;
		else
			return false;
	}

	public function syncPluginsData(){
		$query = "SELECT `id` FROM `wp_plugins` WHERE `tested` != 0 ORDER BY `id` DESC LIMIT 1";
		$res = $this->query($query);

		if($res !== NULL)
			$row = $res->fetch_array();
		else $row = array(array('id' => 0));
		$n = $row['id'];

		for($i=1; $i<=$n; $i++){
			$query = "
				UPDATE `wp_plugins`
				SET
					`usage` = (
						SELECT COUNT(*) FROM  `wp_sites` WHERE `plugins_have` LIKE '$i,%' OR `plugins_have` LIKE '%,$i,%'
					),
					`tested` = (
						SELECT COUNT( * ) FROM  `wp_sites` WHERE  `plugins_checked` LIKE '$i,%' OR `plugins_checked` LIKE '%,$i,%'
					)
				WHERE `id` = $i";

			$this->query($query);
		}
	}
}
