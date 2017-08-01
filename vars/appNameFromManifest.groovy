// vars/appNameFromManifest.groovy
def call(boolean isFeature = false) {
    def appInfo = readYaml file: './manifest.yml'
    def baseName = appInfo.applications[0].name

    if (isFeature) {
        def branch = env.BRANCH_NAME.replace('/','-')
        println "baseName: ${baseName}"
        println "branch: ${branch}"
        println "together?"
        def foo = "${baseName}-${branch}"
        println foo
    }
    return baseName
}
