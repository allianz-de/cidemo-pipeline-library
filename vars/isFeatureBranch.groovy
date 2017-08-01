def call() {
    println "env?"
    println env.BRANCH_NAME

    def branch = env.BRANCH_NAME
    return env.BRANCH_NAME.startsWith('feature/')
}
