// vars/appNameFromManifest.groovy
def call(boolean isFeature = false) {
    def appInfo = readYaml file: './manifest.yml'
    def baseName = appInfo.applications[0].name

    if (isFeature) {
        println "helper says feature too"
        def branch = env.BRANCH_NAME.replace('/','-')
        println "baseName: ${baseName}"
        println "branch: ${branch}"
        println "together?"
        def foo = "${baseName}-${branch}"
        println foo
    } else {
        println "helper says not a feature"
    }

    return baseName
}
