// vars/uploadToArtifactory.groovy
def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    node {
        String artifactoryUrl = config.hasProperty('artifactoryUrl') ? config[artifactoryUrl] : "http://${env.LOCAL_IP}:8000/artifactory"
        String credentialsId = config.hasProperty('credentialsId') ? config[credentialsId] : 'artifactory'

        def server = Artifactory.newServer url: artifactoryUrl, credentialsId: credentialsId
        def uploadSpec = """{
              "files": [
                {
                  "pattern": "${config.pattern}",
                  "target": "${config.target}"
                }
             ]
            }"""
        def buildInfo = server.upload(uploadSpec)
        server.publishBuildInfo(buildInfo)
    }
}
