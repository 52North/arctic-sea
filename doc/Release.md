# Release

How to perform a release of iceland to Maven central.

## Prerequisites

Put your [Sonatype OSS](https://oss.sonatype.org/) credentials into your `.m2/settings.xml` like this:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>ossrh</id>
            <username>$username</username>
            <password>$password</password>
        </server>
    </servers>
</settings>
```

## Release execution

The branches ``master`` and ``develop`` are protected in iceland, therefore changes can only be pushed there via pull requests. The release should be done in extra branches for both develop and master, from which we merge to both the main repository.

Note: It is important to call `release:perform` **after** the changes are pushed to the actual branches as the plugin will checkout these.

```sh
# first develop
git checkout develop
# pull latest changes
git pull --ff upstream/develop
# create a release branch
git checkout -b release/develop
# push it to remote
git push -u origin release/develop

# prepare the release, note down the release tag name
mvn release:prepare
# push the changes
git push origin

# now master...
git checkout master
# create a second release branch
git checkout -b release/master
# set it to the right commit
git merge --ff-only <release version tag, e.g. v1.1.0>
# push it to remote
git push -u origin release/master

# CREATE SEPERATE PULL REQUESTS AND MERGE THEM
# 1. from <your fork>/release/master to 52North/master
# 2. from <your fork>/release/develop to 52North/develop

# remove the remote release branches
git push origin --delete release/master release/develop

# back to master
git checkout master
#  pull latest changes (inkluding the actual release)
git pull --ff upstream/master
# push it to remote
git push origin
# publish the release
mvn release:perform -P sign
```

After performing the release on the command line, log in to Sonatype Nexus at https://oss.sonatype.org/ and complete the following steps:

* Locate the project in the the staging repository: https://oss.sonatype.org/#stagingRepositories
* Check it contains the required files (pom, asc, sources, javadoc, ...)
* "Close" the staging repository
* Click on "Release" (might have to "Refresh" first)
* Wait... then refresh - the staging repo should be gone.
* Check after some delay for the published modules at http://repo1.maven.org/maven2/org/n52/iceland/