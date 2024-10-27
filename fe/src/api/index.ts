import { makeApi, Zodios, type ZodiosOptions } from "@zodios/core";
import { z } from "zod";

type GameResponse = {
  id: number;
  state: string;
  gameConfig: {
    roles: Array<RoleDescription>;
  };
};
type RoleDescription = {
  name: string;
  team: string;
  count: number;
};

const RoleDescription: z.ZodType<RoleDescription> = z
  .object({ name: z.string(), team: z.string(), count: z.number().int() })
  .strict()
  .passthrough();
const GameResponse: z.ZodType<GameResponse> = z
  .object({
    id: z.number().int(),
    state: z.string(),
    gameConfig: z
      .object({ roles: z.array(RoleDescription) })
      .strict()
      .passthrough(),
  })
  .strict()
  .passthrough();

export const schemas = {
  RoleDescription,
  GameResponse,
};

const endpoints = makeApi([
  {
    method: "post",
    path: "/games",
    alias: "createGame",
    requestFormat: "json",
    response: z.object({ id: z.number().int() }).strict().passthrough(),
  },
  {
    method: "get",
    path: "/games/:id",
    alias: "getGame",
    requestFormat: "json",
    parameters: [
      {
        name: "id",
        type: "Path",
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
