// vars/cfPush.groovy
def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // now build, based on the configuration provided
    node {
        String apiUrl        = requiredParam(config, 'apiUrl')
        String credentialsId = requiredParam(config, 'credentialsId')
        String org           = requiredParam(config, 'org')
        String space         = requiredParam(config, 'space')
        String skipSSL       = config.hasProperty('skipSSL') && config['skipSSL']
                                ? '--skip-ssl-validation'
                                : ''

        withCredentials([[
            $class          : 'UsernamePasswordMultiBinding',
            credentialsId   : "${credentialsId}",
            usernameVariable: 'CF_USER',
            passwordVariable: 'CF_PASSWORD' ]]) {

            String loginOptions = "-a $apiUrl -u $CF_USER -p $CF_PASSWORD -o $org -s $space $skipSSL"

            String pushOpts = ''
            if (config.hasProperty('appName')) {
                pushOpts = pushOpts + " ${config['appName']}"
            }
            if (config.hasProperty('manifest')) {
                pushOpts = pushOpts + " -f ${config['manifest']}"
            }

            sh "cf login $loginOptions"
            sh "cf push $pushOptions"
            sh "cf logout"
        }
    }
}

def requiredParam(Map config, String param) {
    if (config.hasProperty(param)) {
        return config[param]
    } else {
        error "missing $param parameter"
    }
}
