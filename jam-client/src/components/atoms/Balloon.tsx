import { Graphics } from 'pixi.js';
import { PixiComponent } from '@inlet/react-pixi';

interface Props {
  x: number;
  y: number;
  width: number;
  height: number;
  color: number;
}

export const Balloon = PixiComponent<Props, Graphics>('Balloon', {
  create: () => new Graphics(),
  applyProps: (ins, _, props) => {
    ins.clear();
    ins.lineStyle(2, 0xff00ff, 1);
    ins.beginFill(0x650a5a, 0);
    ins.drawRoundedRect(props.x, props.y, props.width, props.height, 16);
    ins.endFill();
  },
});
