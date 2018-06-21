# CI Demo - Pipeline Library

When creating custom pipelines, shared code and patterns can be extrapolated into [shared libraries](https://jenkins.io/doc/book/pipeline/shared-libraries/), which are imported into your pipelines like so:

```groovy
@Library('demo-pipeline-library') _
```

This is a working library and features integrations with

- [Cloud Foundry](https://www.cloudfoundry.org/)
    - [`appNameFromManifest`](./vars/appNameFromManifest.groovy)
    - [`cfPush`](./vars/cfPush.groovy)
- [Artifactory](https://www.jfrog.com/artifactory/)
    - [`cleanUpArtifacts`](./vars/cleanUpArtifacts.groovy)
    - [`createArtifact`](./vars/createArtifact.groovy)
    - [`uploadToArtifactory`](./vars/uploadToArtifactory.groovy)

## Shared Library

This demo pipeline library includes global variables defined in groovy files in the `vars/` subfolder, which are singletons and automatically instantiated:

### Global Functionality

Consider making these methods available to all teams across your organization.

- `appNameFromManifest`
- `cfPush`
- `buildCommitSha`
- `cleanUpArtifacts`
- `createArtifact`
- `uploadToArtifactory`

### Team Specific Conventions

You can also use pipeline libraries to make sure teams adhere to their specific naming conventions. See these examples:

- `nextVersion`
- `isFeatureBranch`
- `isReleaseBranch`

## Domain Specific Language

Some are standard helpers like `isFeatureBranch()` which commmunicate _what_ is happening and hide the _how_. Others accept closures, for example `createArtifact` or `uploadToArtifactory`, which do the same and enable a declarative syntax that is easier to understand (and debug!)

```groovy
post {
    success {
        script {
            createArtifact {
                prefix = 'artifact-'
                version = nextVersion()
                sha = buildCommitSha()
            }

            uploadToArtifactory {
                pattern = 'artifact-*.zip'
                target = 'snapshot-local/cidemo-frontend/'
            }

            cleanUpArtifacts()
        }
    }
}
```

When you create libraries that you want to be used, pay attention to the design of your domain specific language (DSL). A beautiful DSL let's developers ship without worrying about _how_ something works. Aim for clear DSLs for greater long-term productivity.
