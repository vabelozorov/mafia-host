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

### Notes

1. To re-generate the code for TS OpenAPI client, execute from the `fe` directory:
`npx openapi-zod-client ..\openapi\openapi.yaml -o 'src/api/index.ts' --strict-objects  --implicit-required   --export-schemas --export-types`
