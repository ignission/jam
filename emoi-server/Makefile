.DEFAULT_GOAL := help

dev: ## For development (server + client)
	docker-compose up -d kms && sbt "~reStart --- -Dconfig.resource=local.conf"

dev-test:
	docker-compose up -d kms && sbt "~test" -Dconfig.resource=test.conf

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'