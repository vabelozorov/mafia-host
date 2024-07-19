# Mafia Host App

### FE

Install latest Node.JS. Then

 ```
    cd fe
    npm install
```

Launch FE with

`npm run dev`


### BE

Install Docker Desktop. 
Then add a `--spring.profiles.active=docker` program argument to your Intellij IDEA launch configuration.
Postgres DB container should be launched automatically.

Whatever DB init scripts you have, put them inside `db/init`