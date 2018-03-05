start powershell {mvn compile exec:java '-Dexec.mainClass=com.kmurawska.playground.akka.transformation.TransformationClientApp' '-Dexec.args=2551'}
start powershell {mvn compile exec:java '-Dexec.mainClass=com.kmurawska.playground.akka.transformation.TransformationWorkerApp' '-Dexec.args=2552'}
start powershell {mvn compile exec:java '-Dexec.mainClass=com.kmurawska.playground.akka.transformation.TransformationWorkerApp' '-Dexec.args=2553'}
start powershell {mvn compile exec:java '-Dexec.mainClass=com.kmurawska.playground.akka.transformation.TransformationWorkerApp' '-Dexec.args=0'}