# JavaScript CI Demo - Jenkins Pipeline Library

MunichJS  
August 2017

### What is a Pipeline Library?

When creating custom pipelines, shared code and patterns can be extrapolated into [shared libraries](https://jenkins.io/doc/book/pipeline/shared-libraries/), which are imported into your pipelines like so:

```
@Library('demo-pipeline-library') _
```

### Creating a Library

This demo pipeline library includes global variables defined in groovy files in the `vars/` subfolder, which are singletons and automatically instantiated:

- `appNameFromManifest`
- `buildCommitSha`
- `cleanUpArtifacts`
- `createArtifact`
- `isFeatureBranch`
- `isReleaseBranch`
- `nextVersion`
- `uploadToArtifactory`

### Define a custom DSL

Some are standard helpers like `isFeatureBranch()` which commmunicate _what_ is happening and hide the _how_. Others accept closures, for example `createArtifact` or `uploadToArtifactory`, which do the same and enable a declarative syntax that is easier to understand (and debug!)

```
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

## License (MIT)

Copyright (c) 2017 Julie Ng

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
