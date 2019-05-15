#!/bin/sh

#
# Script to transfer configuration parameters from 
# environment variables to a Faroe configuration 
# using either the SQLITE or the JSON backend.
#
# Settings are applied to the configuration in 
# ${FAROE_CONFIGURATION}.
#
# Environment variables are expected to be all uppercase
# with only underscores between the words. The mapping
# depends on the exising keys in the configuration and
# the environment variables will be matched against these,
# e.g. SERVICE_IDENTIFICATION_TITLE will be applied to 
# the setting serviceIdentification.title.
#
# Multilingual settings are expected to be in their JSON 
# encoding, e.g.: '{"en":"english","de":"deutsch"}'.
#
# All command line arguments will be forwarded to exec.
#
# Requirements:
# - jq
# - sqlite3 (only for SQLITE configurations)
# - tr
# - sed
#


set -e -o pipefail

function log() { echo $* >&2; }

function identifier_to_env() {
   sed -E -e 's|([a-z])([A-Z])|\1_\2|g; s|[-.]|_|g' | tr '[:lower:]' '[:upper:]'
}

function process_settings_sqlite() {
  local ENV_VARIABLE KEY VALUE TYPE
  get_setting_identifiers_sqlite | while read KEY; do
    ENV_VARIABLE="$(echo "${KEY}" | identifier_to_env)"
    if printenv "${ENV_VARIABLE}" 2>&1 >/dev/null; then
      VALUE="$(printenv "${ENV_VARIABLE}")"
      TYPE="$(get_setting_type_sqlite "${KEY}")"
      log "Applying ${ENV_VARIABLE} to setting ${KEY} of type ${TYPE}, value: ${VALUE}"
      set_setting_sqlite "${KEY}" "${VALUE}" "${TYPE}"
    fi
  done
}

function get_setting_identifiers_sqlite() {
  sqlite3 "${FAROE_CONFIGURATION}" 'select identifier from settings;' | sort 
}

function get_setting_type_sqlite() {
  sqlite3 "${FAROE_CONFIGURATION}" "
    SELECT 'boolean' As type FROM boolean_settings WHERE identifier = '$KEY'
    UNION SELECT 'file' As type FROM file_settings WHERE identifier = '$KEY'
    UNION SELECT 'integer' As type FROM integer_settings WHERE identifier = '$KEY'
    UNION SELECT 'numeric' As type FROM numeric_settings WHERE identifier = '$KEY'
    UNION SELECT 'string' As type FROM string_settings WHERE identifier = '$KEY'
    UNION SELECT 'timeInstant' As type FROM time_settings WHERE identifier = '$KEY'
    UNION SELECT 'uri' As type FROM uri_settings WHERE identifier = '$KEY'
    UNION SELECT 'multilingual' As type FROM multilingual_string_settings WHERE identifier = '$KEY'"
}

function set_setting_sqlite() {
  local KEY="$1"
  local VALUE="$2"
  local TYPE="$3"
  local SQL

  if [ "${TYPE}" == "multilingual" ]; then
    TABLE='multilingual_string_settings_values'
    SQL="DELETE FROM ${TABLE} WHERE identifier = '${KEY}';"
    
    local IFS=$'\n'
    for VALUES in $(echo "${VALUE}" | jq -r --jsonargs 'to_entries[] | "'"'"'\(.key)'"'"', '"'"'\(.value)'"'"'"'); do
      SQL="${SQL} INSERT INTO ${TABLE} (identifier, lang, value) VALUES ('${KEY}', ${VALUES});"
    done
    sqlite3 "${FAROE_CONFIGURATION}" "${SQL}"
  else
    case "${TYPE}" in
      string)       TABLE='string_settings' ;;
      choice)       TABLE='choice_settings' ;;
      file)         TABLE='file_settings' ;;
      uri)          TABLE='uri_settings' ;;
      timeInstant)  TABLE='time_settings' ;;
      boolean)      TABLE='boolean_settings' ;;
      integer)      TABLE='integer_settings' ;;
      number)       TABLE='numeric_settings' ;;
      *) log "WARN: unsupported settings type: ${TYPE}" ;;
    esac
    if [ "${VALUE}" == "null" ]; then
      VALUE='NULL'
    else
      case "${TYPE}" in string|choice|file|uri|time) VALUE="'${VALUE}'" ;; esac
    fi    
    sqlite3 "${FAROE_CONFIGURATION}" "UPDATE ${TABLE} SET value = ${VALUE} WHERE identifier = '${KEY}'"
  fi 

}

function process_settings_json() {
  local ENV_VARIABLE KEY VALUE TYPE
  get_setting_identifiers_json | while read KEY; do
    ENV_VARIABLE="$(echo "${KEY}" | identifier_to_env)"
    if printenv "${ENV_VARIABLE}" 2>&1 >/dev/null; then
      VALUE="$(printenv "${ENV_VARIABLE}")"
      TYPE="$(get_setting_type_json "${KEY}")"
      log "Applying ${ENV_VARIABLE} to setting ${KEY} of type ${TYPE}, value: ${VALUE}"
      set_setting_json "${KEY}" "${VALUE}" "${TYPE}"
    fi
  done
}

function get_setting_identifiers_json() {
  jq -r '.settings | keys[]' "${FAROE_CONFIGURATION}"
}

function get_setting_type_json() {
  local KEY="$1"
  jq -r --arg key "${KEY}" '.settings[$key].type' "${FAROE_CONFIGURATION}"
}

function set_setting_json() {
  local KEY="$1"
  local VALUE="$2"
  local TYPE="$3"
  case "${TYPE}" in
    # string based settings, add quotes
    string|choice|file|uri|timeInstant)
      if [ "${VALUE}" != "null" ]; then VALUE="\"${VALUE}\""; fi ;;
    # basic json values, do nothing
    boolean|integer|number) ;;
    # complex json values, do noting
    multilingual) ;;
    *) log "WARN: unsupported settings type: ${TYPE}" ;;
  esac

  local TEMP="$(mktemp)"
  jq --arg key "${KEY}" --argjson value "${VALUE}" '.settings[$key].value = $value' "${FAROE_CONFIGURATION}" > "${TEMP}"
  mv -f "${TEMP}" "${FAROE_CONFIGURATION}"
}

if [ -z "${FAROE_CONFIGURATION}" ]; then
  log "FAROE_CONFIGURATION is not defined"
  exit 1
fi

if [ ! -f "${FAROE_CONFIGURATION}" ]; then
  log "${FAROE_CONFIGURATION} does not exist"
  exit 1
fi

case "${FAROE_CONFIGURATION}" in 
  *json) process_settings_json ;;
  *db) process_settings_sqlite ;;
  *) log "Unrecognized settings format"; exit 1 ;;
esac

exec "$@"
