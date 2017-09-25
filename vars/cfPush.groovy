// vars/cfPush.groovy
def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // now build, based on the configuration provided
    node {
        // Cloud Foundry login options must be set as environment variables
        String apiUrl        = requiredEnv('CF_API')
        String org           = requiredEnv('CF_ORG')
        String space         = requiredEnv('CF_SPACE')

        // These params can be passed into the `cfPush` closure
        String credentialsId = requiredParam(config, 'credentialsId')
        String skipSSL       = config.hasProperty('skipSSL') && config['skipSSL']
                                ? '--skip-ssl-validation'
                                : ''

        // Optional params
        String pushOpts = ''
        if (config.hasProperty('appName')) {
            pushOpts = pushOpts + " ${config['appName']}"
        }
        if (config.hasProperty('manifest')) {
            pushOpts = pushOpts + " -f ${config['manifest']}"
        }

        withCredentials([[
            $class          : 'UsernamePasswordMultiBinding',
            credentialsId   : "${credentialsId}",
            usernameVariable: 'CF_USER',
            passwordVariable: 'CF_PASSWORD' ]]) {

            String loginOptions = "-a $apiUrl -u $CF_USER -p $CF_PASSWORD -o $org -s $space $skipSSL"

            sh "cf login $loginOptions"
            sh "cf push $pushOpts"
            sh "cf logout"
        }
    }
}

def requiredEnv(String param) {
    if (env[param]) {
        return env[param]
    } else {
        error "Missing $param param as environment variable"
    }
}


def requiredParam(Map config, String param) {
    if (config.hasProperty(param)) {
        return config[param]
    } else {
        error "Missing $param parameter"
    }
}
