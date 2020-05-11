.DEFAULT_GOAL := help

COMPOSE_COMMAND := docker-compose -f ../docker-compose.yml up -d kms db

dev: ## For development (server + client)
	${COMPOSE_COMMAND} && sbt "~reStart --- -Dconfig.resource=local.conf"

dev-test:
	${COMPOSE_COMMAND} && sbt "~test" -Dconfig.resource=test.conf

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'