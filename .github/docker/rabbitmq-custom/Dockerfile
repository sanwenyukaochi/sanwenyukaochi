# syntax=docker/dockerfile:1

ARG RABBITMQ_TAG

FROM rabbitmq:${RABBITMQ_TAG}

ARG DELAYED_PLUGIN_URL

USER root

# Install curl for downloading the RabbitMQ delayed message plugin.
RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl \
    && rm -rf /var/lib/apt/lists/*

# Download the delayed message plugin.
RUN set -eux; \
    curl -fsSL "${DELAYED_PLUGIN_URL}" \
      -o "/plugins/rabbitmq_delayed_message_exchange.ez"; \
    chown rabbitmq:rabbitmq /plugins/rabbitmq_delayed_message_exchange.ez; \
    rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange

USER rabbitmq