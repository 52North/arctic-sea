#!/bin/bash

stop() {
  echo $* >&2
  exit 0
}

[ "${TRAVIS_BRANCH}" == "${SNAPSHOT_BRANCH}" ] \
  || stop "won't build branch ${TRAVIS_BRANCH}"

[ "${TRAVIS_SECURE_ENV_VARS}" == "true" ] \
  || stop "no secure enviroment variables were provided"

[ "${TRAVIS_JOB_NUMBER}" == "${TRAVIS_BUILD_NUMBER}.1" ] \
  || stop "only the first build job will be deployed"

[ "${TRAVIS_PULL_REQUEST}" == "false" ] \
  || stop "no deployment for pull requests"


mvn deploy -DskipTests=true --settings ".travis/maven-settings.xml"


