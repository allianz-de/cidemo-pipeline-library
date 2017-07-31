// vars/createArtifact.groovy
def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // now build, based on the configuration provided
    node {
        String buildDataFile = config.hasProperty('buildDataFile') ? config[buildDataFile] : 'artifact.json'
        String prefix = config.hasProperty('prefix') ? config[prefix] : 'artifact-'

        String branch = env.BRANCH_NAME.replace('/','-')
        String buildData = """
        {
            "version": "${config.version}+${config.sha}",
            "type": "snapshot",
            "branch": "${branch}",
            "job": {
                "baseName": "${env.JOB_BASE_NAME}",
                "name": "${env.JOB_NAME}"
            },
            "build": {
                "number": "${env.BUILD_NUMBER}",
                "tag": "${env.BUILD_TAG}",
            }
        }"""
        touch     file: buildDataFile
        writeFile file: buildDataFile, text: buildData, encoding: 'utf-8'

        // Create zip artifact
        String zipFile = "${prefix}${branch}-${config.sha}.zip"

        if (!fileExists(zipFile)) {
            zip zipFile: zipFile
        }
    }
}