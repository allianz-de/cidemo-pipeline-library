// vars/appNameFromManifest.groovy
def call(boolean isFeature = false) {
  def appInfo = readYaml file: './manifest.yml'
  def baseName = appInfo.applications[0].name

  println baseName

  String result = isFeature
    ? "${baseName}-${env.BRANCH_NAME.replace('/','-')}"
    : baseName

  println result

  return baseName
}
