$machineName = "playground"

$dockerMachines = docker-machine ls 
if ($dockerMachines -Like "*playground*") {
	echo "Starting docker machine..."
	docker-machine start $machineName
	docker-machine env $machineName
	&docker-machine env $machineName | Invoke-Expression
	echo "Docker machine started."
} else {
	echo "Creating docker machine..." 
	docker-machine create --driver virtualbox --virtualbox-disk-size "30000" --virtualbox-memory "4096" --virtualbox-cpu-count "2" --virtualbox-hostonly-cidr "192.168.90.1/24" $machineName 
	docker-machine start $machineName 
	docker-machine env $machineName
	&docker-machine env $machineName | Invoke-Expression
	echo "Docker machine created."

    echo "Adding shared folders..."
    $playground = [System.IO.Path]::GetFullPath((Join-Path (pwd) '..\playground'))
    &"C:\Program Files\Oracle\VirtualBox\VBoxManage" sharedfolder add $machineName --name "/etc/playground" --hostpath "$playground" --automount
    echo "Shared folders added."
    echo ""

    docker-machine start $machineName

	echo "Setting ports forwarding on Oracle VirtualBox machine..."
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "cassandra CQL,tcp,,9042,,9042"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "cassandra JMX,tcp,,7199,,7199"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "mongodb,tcp,,27017,,27017"
	echo "Setting ports forwarding finished."

    docker-machine env $machineName
    &docker-machine env $machineName | Invoke-Expression
}