# PROJECT WEB SHOP


## Description

Welcome to Buy Our Stuff!
It is a Java/SQL project where back-end and front-end go hand-in-hand. The server can be run either from memory, from database or from file. The program displays a website. It is a webshop with classic features: you can log in, browse products and add them to cart and purchase products  online. In our webshop you find quality products that support a calm mind, a relaxed body and an overall mental well-being. Happy shopping, far out man!

## How to run in IntelliJ:

1. import as a Maven project
2. create run/debug configuration
     - create maven template
     - choose working directory
     - set command line parameter to `jetty:run -f pom.xml`
     - set environment variables to `DAO_TYPE=?;DB_USER_NAME=codecool;DB_PASSWORD=c0d3c00l` where ? is
         - `memory` for memory dao implementation usage
         - `database` for database dao implementation usage 
         - `csv` for csv file dao implementation usage
         - `json` for json file implementation usage
      - name your configuration
      - press apply and ok button
3. run configuration
4. open `localhost:8888` in the browser
5. don't worry, be happy :)

## Created by

- Szakál Zsófia
- Csanádi Balázs
- Iszály Zsolt
- Kuti Erik
- Ormay Szabolcs
- Vasmatics András

## Background materials

- <i class="far fa-exclamation"></i> Codecool buy our staff starter project
- <i class="far fa-exclamation"></i> [Google search](https://www.google.com)
