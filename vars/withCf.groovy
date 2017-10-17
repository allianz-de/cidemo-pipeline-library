// vars/withCf.groovy
def call(Map params = [:], Closure body) {
    String apiUrl        = requiredParam(params, 'apiUrl')
    String credentialsId = params['credentialsId']
                            ? params['credentialsId']
                            : 'pcf'
    String org           = requiredParam(params, 'org')
    String space         = requiredParam(params, 'space')
    String skipSSL       = params['skipSSL']
                            ? '--skip-ssl-validation'
                            : ''

    // use unique cf config per build
    withEnv(["PATH+CF_HOME=${tool name: 'cf_cli'}", "CF_HOME=${env.WORKSPACE}/.cf/${env.BUILD_NUMBER}"]) {
        try {
            sh "mkdir -p '$CF_HOME'"

            withCredentials([[
                $class          : 'UsernamePasswordMultiBinding',
                credentialsId   : "$credentialsId",
                usernameVariable: 'CF_USER',
                passwordVariable: 'CF_PASSWORD' ]]) {

                String loginOptions = "-a $apiUrl -u '$CF_USER' -p '$CF_PASSWORD' -o $org -s $space $skipSSL"
                sh "cf login $loginOptions"

                body()

                sh "cf logout"
            }
        } finally {
            sh "if [ -e '$CF_HOME' ]; then rm -Rf '$CF_HOME'; fi"
        }
    }
}

def requiredParam(Map params, String key) {
    if (params[key]) {
        return params[key]
    } else {
        error "missing $key parameter"
    }
}
