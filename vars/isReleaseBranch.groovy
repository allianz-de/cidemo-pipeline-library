// vars/isReleaseBranch.groovy
def call() {
    return env.BRANCH_NAME == 'release'
}
