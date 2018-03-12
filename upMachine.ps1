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
	
	echo "Fixing incorrect network adapter type..."
	docker-machine stop $machineName
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" modifyvm playground --nictype1 Am79C973
	echo "Network adapter type fixed."

    echo "Adding shared folders..."
    $playground = [System.IO.Path]::GetFullPath((Join-Path (pwd) '..\playground'))
    &"C:\Program Files\Oracle\VirtualBox\VBoxManage" sharedfolder add $machineName --name "/etc/playground" --hostpath "$playground" --automount
    echo "Shared folders added."
    echo ""

    docker-machine start $machineName

	echo "Setting ports forwarding on Oracle VirtualBox machine..."
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "CA,tcp,,7054,,7054"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "orderer,tcp,,7050,,7050"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "peer0,tcp,,7051,,7051"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "peer0_events,tcp,,7053,,7053"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "peer1,tcp,,7151,,7151"
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "peer1_events,tcp,,7153,,7153"
	echo "Setting ports forwarding finished."

    docker-machine env $machineName
    &docker-machine env $machineName | Invoke-Expression
}