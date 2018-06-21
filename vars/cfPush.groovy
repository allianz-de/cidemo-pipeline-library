// vars/cfPush.groovy

// This `cfPush` is dumb (does login and logout too) and for demo purposes only.
// It is not meant for development or production use.

def call(Map params = [:]) {
    String apiUrl        = requiredParam(params, 'apiUrl')
    String credentialsId = requiredParam(params, 'credentialsId')
    String org           = requiredParam(params, 'org')
    String space         = requiredParam(params, 'space')

    // skipSSL was required when deploying to locally installed pcf-dev
    String skipSSL       = params['skipSSL']
                            ? '--skip-ssl-validation'
                            : ''

    withCredentials([[
        $class          : 'UsernamePasswordMultiBinding',
        credentialsId   : "${credentialsId}",
        usernameVariable: 'CF_USER',
        passwordVariable: 'CF_PASSWORD' ]]) {

        String loginOptions = "-a $apiUrl -u $CF_USER -p $CF_PASSWORD -o $org -s $space $skipSSL"

        String pushOpts = ''
        if (params['appName']) {
            pushOpts = pushOpts + " ${params['appName']}"
        }
        if (params['manifest']) {
            pushOpts = pushOpts + " -f ${params['manifest']}"
        }

        sh "cf login $loginOptions"
        sh "cf push $pushOpts"
        sh "cf logout"
    }

}

def requiredParam(Map params, String key) {
    if (params[key]) {
        return params[key]
    } else {
        error "missing $key parameter"
    }
}
