.DEFAULT_GOAL := help

up: ## Start
	docker-compose pull server
	docker-compose up -d
	docker-compose logs -f

down: ## Stop and destroy
	docker-compose down

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'