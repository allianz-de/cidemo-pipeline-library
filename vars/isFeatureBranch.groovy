// vars/isFeatureBranch.groovy
def call() {
    return env.BRANCH_NAME.startsWith('feature/')
}
