def call() {
    return sh(script: "npm run version:next | tail -n 1", returnStdout: true).trim()
}
