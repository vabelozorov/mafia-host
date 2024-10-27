import { makeApi, Zodios, type ZodiosOptions } from "@zodios/core";
import { z } from "zod";

type GameResp = {
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
const GameResp: z.ZodType<GameResp> = z
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
const PlayerAssignmentReq = z
  .object({
    players: z
      .array(
        z
          .object({ name: z.string().min(1).max(50), role: z.string() })
          .strict()
          .passthrough()
      )
      .min(1),
  })
  .strict()
  .passthrough();

export const schemas = {
  RoleDescription,
  GameResp,
  PlayerAssignmentReq,
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
    method: "post",
    path: "/games/:gameId/players",
    alias: "submitPlayersConfiguration",
    description: `Assigns roles to all players in the game, including their names and positions`,
    requestFormat: "json",
    parameters: [
      {
        name: "body",
        type: "Body",
        schema: PlayerAssignmentReq,
      },
      {
        name: "gameId",
        type: "Path",
        schema: z.number().int(),
      },
    ],
    response: z.void(),
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
    response: GameResp,
  },
]);

export const api = new Zodios(endpoints);

export function createApiClient(baseUrl: string, options?: ZodiosOptions) {
  return new Zodios(baseUrl, endpoints, options);
}
