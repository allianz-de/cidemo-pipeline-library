// vars/appNameFromManifest.groovy
def call(boolean isFeature = false) {
    def appInfo = readYaml file: './manifest.yml'
    def baseName = appInfo.applications[0].name

    if (isFeature) {
        def branch = env.BRANCH_NAME.replace('/','-')
        echo "baseName: ${baseName}"
        echo "branch: ${branch}"
        echo "together?"
        def foo = "${baseName}-${branch}"
        println foo
    }
    return baseName
}
