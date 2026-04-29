#!/usr/bin/env bash
set -euo pipefail

UPSTREAM_REPO="library/rabbitmq"
PLUGIN_REPO="rabbitmq/rabbitmq-delayed-message-exchange"

tags_file="$(mktemp)"
url="https://hub.docker.com/v2/repositories/${UPSTREAM_REPO}/tags?page_size=100"

while [[ -n "$url" && "$url" != "null" ]]; do
  response="$(curl -fsSL "$url")"

  echo "$response" \
    | jq -r '.results[].name' \
    >> "$tags_file"

  url="$(echo "$response" | jq -r '.next')"
done

rabbitmq_tag="$(cat "$tags_file" \
  | grep -E '^[0-9]+\.[0-9]+\.[0-9]+-management$' \
  | sort -V \
  | tail -n 1)"

if [[ -z "$rabbitmq_tag" ]]; then
  echo "Failed to detect latest RabbitMQ management tag"
  exit 1
fi

rabbitmq_major_minor="$(echo "$rabbitmq_tag" | sed -E 's/^([0-9]+\.[0-9]+)\..*$/\1/')"

releases_json="$(curl -fsSL "https://api.github.com/repos/${PLUGIN_REPO}/releases?per_page=100")"

plugin_version="$(echo "$releases_json" \
  | jq -r '.[].tag_name' \
  | grep -E "^v${rabbitmq_major_minor}\.[0-9]+$" \
  | sed 's/^v//' \
  | sort -V \
  | tail -n 1)"

if [[ -z "$plugin_version" ]]; then
  echo "No delayed plugin release found for RabbitMQ ${rabbitmq_major_minor}.x"
  exit 1
fi

delayed_plugin_url="$(echo "$releases_json" \
  | jq -r ".[] | select(.tag_name == \"v${plugin_version}\") | .assets[].browser_download_url" \
  | grep -E '\.ez$' \
  | head -n 1)"

if [[ -z "$delayed_plugin_url" ]]; then
  echo "Failed to resolve .ez asset URL for plugin v${plugin_version}"
  exit 1
fi

{
  echo "rabbitmq_tag=${rabbitmq_tag}"
  echo "rabbitmq_major_minor=${rabbitmq_major_minor}"
  echo "delayed_plugin_version=${plugin_version}"
  echo "delayed_plugin_url=${delayed_plugin_url}"
} >> "$GITHUB_OUTPUT"