// vars/randomPort.groovy
def call() {
    return Math.abs(new Random().nextInt() % 8000) + 1000
}
