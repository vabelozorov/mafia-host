import { makeApi, Zodios, type ZodiosOptions } from "@zodios/core";
import { z } from "zod";

type GameResponse = {
  id: number;
  state: GameState;
};
type GameState = "assigning_roles";

const GameState = z.literal("assigning_roles");
const GameResponse: z.ZodType<GameResponse> = z
  .object({ id: z.number().int(), state: GameState })
  .strict()
  .passthrough();

export const schemas = {
  GameState,
  GameResponse,
};

const endpoints = makeApi([
  {
    method: "post",
    path: "/game",
    alias: "createGame",
    requestFormat: "json",
    response: z.object({ id: z.number().int() }).strict().passthrough(),
  },
  {
    method: "get",
    path: "/game",
    alias: "getGame",
    requestFormat: "json",
    parameters: [
      {
        name: "id",
        type: "Query",
        schema: z.number().int(),
      },
    ],
    response: GameResponse,
  },
]);

export const api = new Zodios(endpoints);

export function createApiClient(baseUrl: string, options?: ZodiosOptions) {
  return new Zodios(baseUrl, endpoints, options);
}
