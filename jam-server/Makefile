.DEFAULT_GOAL := help

dev: ## For development
	docker-compose up -d kms db && make dev-only

dev-only: ## For root call
	sbt "server/flywayMigrate; ~server/reStart --- -Dconfig.resource=local.conf"

dev-test: ## Test with reloading
	docker-compose up -d kms db-test && sbt "~test" -Dconfig.resource=test.conf

fixmat: ## Format code and fix
	sbt "format; fix"

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'