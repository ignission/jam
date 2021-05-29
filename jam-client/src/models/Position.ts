export interface Position {
  x: number;
  y: number;
}

export const Position = (x: number, y: number): Position => ({ x, y });
