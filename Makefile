.DEFAULT_GOAL := help

init: ## Initialize and set up local development environment
	cd jam-client && \
	yarn && \
	yarn build && \
	cd ../

up: ## Start
	docker-compose pull server
	docker-compose up -d http websocket kms db
	docker-compose logs -f

down: ## Stop and destroy
	docker-compose down -v

dev-http: ## Start http server with hot-reload
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d http  

kill: ## Kill all containers
	docker-compose kill

dev: ## For development (server + client)
	docker-compose up -d kms db && \
	cd jam-client && yarn hot & \
	cd jam-server && make dev-only-http & \
	cd jam-server && make dev-only-websocket || \
	cd ../ ; jobs -p | xargs kill && \
	docker-compose kill kms

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
