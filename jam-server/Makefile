.DEFAULT_GOAL := help

dev: ## For development
	docker-compose up -d kms db && make dev-only

dev-only: ## For root call
	sbt "server/flywayMigrate; ~server/reStart --- -Dconfig.resource=local.conf"

dev-test: ## Test with reloading
	docker-compose up -d kms db-test
	make NAME=jam-db-test wait-db	
	sbt "~test" -Dconfig.resource=test.conf
	@docker rm -f jam-db-test

test: ## Test
	docker-compose up -d db-test 
	make NAME=jam-db-test wait-db
	sbt test -Dconfig.resource=test.conf
	@docker rm -f jam-db-test

fixmat: ## Format code and fix
	sbt "format; fix"

wait-db: ## Wait until database starts up. Arguments: NAME DB_NAME
	@docker exec ${NAME} \
		bash -c ' \
			until echo \\'\q\\' | mysql -u"jam" -p"jam" "jam" ; do \
				>&2 echo "**** MySQL is unavailable - sleeping" && \
				sleep 1; \
			done \
		'

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'