// vars/appNameFromManifest.groovy
def call(Map params = [:]) {
    def appInfo = readYaml file: './manifest.yml'
    def baseName = appInfo.applications[0].name
    if (params['append']) {
        return baseName + '-' + params['append'].replace('/','-')
    } else {
        return baseName
    }
}
