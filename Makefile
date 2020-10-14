.DEFAULT_GOAL := help

DOCKER_COMPOSE_DEV_CMD := docker-compose -f docker-compose.yml -f docker-compose.dev.yml

init: ## Initialize and set up local development environment
	cd jam-client && \
	yarn && \
	yarn build && \
	cd ../ && \
	${DOCKER_COMPOSE_DEV_CMD} build

up: ## Start
	docker-compose pull server
	docker-compose up -d http websocket kms db
	docker-compose logs -f

up-http: ## Start http server 
	docker-compose up -d http

up-websocket: ## Start websocket server
	docker-compose up -d websocket

dev-http: ## Start http server with hot-reload
	$(DOCKER_COMPOSE_DEV_CMD) up -d http  

dev-websocket: ## Start websocket server with hot-reload
	${DOCKER_COMPOSE_DEV_CMD} up -d websocket

kill: ## Kill all containers
	docker-compose kill

down: ## Stop and destroy
	docker-compose down -v

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
