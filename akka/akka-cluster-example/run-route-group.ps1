start powershell {mvn compile exec:java '-Dexec.mainClass=com.kmurawska.playground.akka.route.group.WorkerApp' '-Dexec.args=2551'}
start powershell {mvn compile exec:java '-Dexec.mainClass=com.kmurawska.playground.akka.route.group.WorkerApp' '-Dexec.args=2552'}
start powershell {mvn compile exec:java '-Dexec.mainClass=com.kmurawska.playground.akka.route.group.ClientApp' '-Dexec.args=0'}
