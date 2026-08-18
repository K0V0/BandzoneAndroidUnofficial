# Claude AI Agent in role of Android developer

## Application architecture, tech stack of project

- available in the ```claude.md```

## Test execution rules

- no tests to run yet

## Branching rules

- never push into main/master branch
- create branch according to the nature of the changes
  - ```feature/<issue_title>``` for work that is adding new features
  - ```bugfix/<issue_title>``` if the intention of the task is bugfixing
  - ```other/<issue_title>``` if the nature of the task is not clear

## Related applications or other microservices

### Scraper API

This android application is using Scraper REST API, which is standart SpringBoot backend application that is responsible 
for scraping webpages and providing endpoints producing uniform JSON output.

- On local filesystem application can be found in ```/home/kovo/IdeaProjects/music-pages-scraper-backend```
- The backend is in private organization repository ```git@github.com:Kovospace/music-pages-scraper-backend.git```
- This project have a ```springboot-developer``` agent at hand to perform certain operations at the usual location
  ```<project_root>/.claude/agents/springboot-developer.md```  