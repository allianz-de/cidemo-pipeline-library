// vars/appNameFromManifest.groovy
def call() {
  def appInfo = readYaml file: './manifest.yml'
  return appInfo.applications[0].name
}
